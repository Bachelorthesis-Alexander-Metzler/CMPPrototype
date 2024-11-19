package de.doubleslash.cmpprototype.data.datasource.local.localDB

import de.doubleslash.cmpprototype.data.datasource.local.localDB.dto.FileDTO

/**
 * Manages local storage operations for files, including saving, retrieving, and deleting files.
 * Utilizes local database to persist file metadata and contents.
 */
interface MongoDB {

    suspend fun addFile(file: FileDTO)
    fun getAllFiles(): List<FileDTO>
    suspend fun deleteFile(file: FileDTO)
}