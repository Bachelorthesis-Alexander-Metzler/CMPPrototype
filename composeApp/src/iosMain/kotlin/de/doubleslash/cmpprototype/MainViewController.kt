package de.doubleslash.cmpprototype

import androidx.compose.ui.window.ComposeUIViewController
import de.doubleslash.cmpprototype.di.initKoin
import kotlinx.cinterop.ExperimentalForeignApi

import platform.Foundation.*

fun MainViewController() = ComposeUIViewController {
    initKoin()
    App()
    processSharedFiles()
}

@OptIn(ExperimentalForeignApi::class)
fun processSharedFiles() {
    val fileManager = NSFileManager.defaultManager()
    val appGroupContainer = fileManager.containerURLForSecurityApplicationGroupIdentifier("group.de.doubleslash.cmpprototype")

    // Inhalte des App Group Containers abrufen
    val sharedFiles = appGroupContainer?.let {
        fileManager.contentsOfDirectoryAtURL(it, includingPropertiesForKeys = null, options = 0u, error = null)
    }

    // Iteriere über die Dateien und verarbeite sie
    sharedFiles?.forEach { file ->
        val fileUrl = file as? NSURL // Sichere Umwandlung in NSURL
        if (fileUrl != null) {
            println("Geteilte Datei gefunden: ${fileUrl.lastPathComponent}")
            saveFileToDatabase(fileUrl)
        } else {
            println("Ungültige Datei gefunden: $file")
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
fun saveFileToDatabase(fileUrl: NSURL) {
    val fileManager = NSFileManager.defaultManager()
    val fileName = fileUrl.lastPathComponent
    val fileContent = NSData.dataWithContentsOfURL(fileUrl)?.bytes
    if (fileContent != null) {
        println("Speichere Datei $fileName in die Datenbank")
        // Logik zum Speichern der Datei in der Datenbank

        // Datei nach erfolgreicher Verarbeitung löschen
        fileManager.removeItemAtURL(fileUrl, error = null)
        println("Datei aus AppGroupContainer gelöscht: $fileName")
    } else {
        println("Fehler: Dateiinhalt konnte nicht gelesen werden.")
    }
}
