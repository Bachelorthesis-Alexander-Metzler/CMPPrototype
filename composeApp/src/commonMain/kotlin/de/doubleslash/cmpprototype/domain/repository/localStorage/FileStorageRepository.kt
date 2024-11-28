package de.doubleslash.cmpprototype.domain.repository.localStorage

import de.doubleslash.cmpprototype.data.datasource.local.localDB.dto.FileDTO

interface FileStorageRepository {
    suspend fun addFile(file: FileDTO)
    fun getAllFiles(): List<FileDTO>
    suspend fun deleteFile(file: FileDTO) : Boolean
}