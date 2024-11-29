package de.doubleslash.cmpprototype.domain.repository.localStorage

import de.doubleslash.cmpprototype.data.datasource.local.localDB.dto.FileDTO
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel
import org.mongodb.kbson.ObjectId

interface FileStorageRepository {
    suspend fun addFile(file: FileDTO)
    fun getAllFiles(): List<FileDTO>
    suspend fun deleteFile(file: FileDTO): Boolean
    fun getFileWithContent(id: ObjectId): FileModel
}