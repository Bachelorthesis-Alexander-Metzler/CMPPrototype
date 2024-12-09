package de.doubleslash.cmpprototype

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ComposeUIViewController
import de.doubleslash.cmpprototype.common.IosMainConstants
import de.doubleslash.cmpprototype.di.initKoin
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel
import de.doubleslash.cmpprototype.domain.use_case.localStorage.SaveLocalFileUseCase
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readBytes
import org.koin.compose.koinInject
import platform.Foundation.*

// Tracks whether an intent has been processed
private var processedIntent by mutableStateOf(false)

fun MainViewController() = ComposeUIViewController {
    initKoin()
    processSharedFiles()
    App(processedIntent = processedIntent)
}

@Composable
@OptIn(ExperimentalForeignApi::class)
fun processSharedFiles() {
    val fileManager = NSFileManager.defaultManager()
    val appGroupContainer = fileManager.containerURLForSecurityApplicationGroupIdentifier(IosMainConstants.APP_GROUP_CONTAINER_ID)

    // Query contents of the App Group container
    val sharedFiles = appGroupContainer?.let {
        fileManager.contentsOfDirectoryAtURL(it, includingPropertiesForKeys = null, options = 0u, error = null)
    }

    // Iterate through files and process them
    sharedFiles?.forEach { file ->
        val fileUrl = file as? NSURL // Safe cast to NSURL
        if (fileUrl != null) {
            println("Shared file found: ${fileUrl.lastPathComponent}")
            saveFileToDatabase(fileUrl)
        } else {
            println("Invalid file found: $file")
        }
    }
}

@Composable
@OptIn(ExperimentalForeignApi::class)
fun saveFileToDatabase(fileUrl: NSURL) {
    val fileManager = NSFileManager.defaultManager()
    val fileName = fileUrl.lastPathComponent
    val fileData = NSData.dataWithContentsOfURL(fileUrl)

    // Convert NSData to ByteArray
    val fileContent = fileData?.let { data ->
        val length = data.length.toInt()
        val byteArray = ByteArray(length)
        data.bytes?.readBytes(length)?.copyInto(byteArray)
        byteArray
    }

    if (fileContent != null) {
        println("Saving file $fileName to the database")

        val saveLocalFileUseCase: SaveLocalFileUseCase = koinInject()

        val fileModel = FileModel(
            baseName = fileName.toString(),
            extension = fileName.toString().substringAfterLast('.'),
            isRemoteFile = false,
            path = fileUrl.path ?: "unknown_path",
            baseTypeId = "cmis:document",
            fileContent = fileContent
        )

        // Start the coroutine in a Composable
        LaunchedEffect(fileModel) {
            print("Invoking SaveLocalFileUseCase")
            saveLocalFileUseCase.invoke(fileModel)
            println("SaveLocalFileUseCase invoked")

            // Mark the intent as processed
            processedIntent = true

            // Delete the file after successful processing
            fileManager.removeItemAtURL(fileUrl, error = null)
            println("File deleted from AppGroupContainer: $fileName")
        }

        // Delete the file after successful processing
        fileManager.removeItemAtURL(fileUrl, error = null)
        println("File deleted from AppGroupContainer: $fileName")
    } else {
        println("Error: File content could not be read.")
    }
}
