package de.doubleslash.cmpprototype

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import de.doubleslash.cmpprototype.data.datasource.local.preferences.appContext
import de.doubleslash.cmpprototype.di.initKoin
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel
import de.doubleslash.cmpprototype.domain.use_case.localStorage.SaveLocalFileUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.core.context.GlobalContext
import java.io.InputStream

class MainActivity : ComponentActivity() {
    // Tracks whether an intent has been processed
    private var processedIntent by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = applicationContext

        // Initialize Koin dependency injection if not already initialized
        if (GlobalContext.getOrNull() == null) {
            initKoin()
        }

        // Handle any incoming share intent
        handleIncomingShare(intent)

        setContent {
            App(processedIntent = processedIntent)
        }
    }

    /**
     * Handles the incoming share intent.
     * If a new intent arrives, it ensures proper processing of the intent.
     */
    private fun handleIncomingShare(intent: Intent?) {
        if (intent != null) {
            super.onNewIntent(intent)
            handleIncomingShareIntent(intent)
        }
    }

    /**
     * Processes the incoming share intent to extract the shared file.
     */
    private fun handleIncomingShareIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_SEND && intent.type != null) {
            val uri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)

            if (uri != null) {
                processSharedFile(uri)
            }
        }
    }

    /**
     * Processes the shared file, retrieves its content, and saves it locally using the SaveLocalFileUseCase.
     */
    private fun processSharedFile(uri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            val contentResolver = contentResolver

            // Open the shared file and read its content
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val fileContent = inputStream?.readBytes()
            inputStream?.close()

            // Retrieve file metadata (e.g., name)
            val cursor = contentResolver.query(uri, null, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val fileNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val fileName = cursor.getString(fileNameIndex)
                cursor.close()

                // Retrieve the SaveLocalFileUseCase from Koin
                val saveLocalFileUseCase: SaveLocalFileUseCase = get()

                // Save the file locally
                uri.path?.let {
                    if (fileContent != null) {
                        saveLocalFileUseCase.invoke(
                            FileModel(
                                baseName = fileName,
                                extension = fileName.substringAfterLast('.'),
                                isRemoteFile = false,
                                path = uri.path,
                                baseTypeId = "cmis:document",
                                fileContent = fileContent
                            )
                        )
                        // Mark the intent as processed
                        processedIntent = true
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
