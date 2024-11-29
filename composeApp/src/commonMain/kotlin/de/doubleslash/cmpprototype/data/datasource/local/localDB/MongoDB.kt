package de.doubleslash.cmpprototype.data.datasource.local.localDB

import de.doubleslash.cmpprototype.data.datasource.local.localDB.dto.FileDTO
import org.mongodb.kbson.ObjectId

/**
 * Manages local storage operations for files, including saving, retrieving, and deleting files.
 * Utilizes local database to persist file metadata and contents.
 */
interface MongoDB {

    suspend fun addFile(file: FileDTO)
    fun getAllFiles(): List<FileDTO>
    suspend fun deleteFile(file: FileDTO): Boolean
    fun getFileWithContent(id: ObjectId): FileDTO
}