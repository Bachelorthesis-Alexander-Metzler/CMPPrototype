package de.doubleslash.cmpprototype.data.repository.localStorage

import de.doubleslash.cmpprototype.data.datasource.local.localDB.MongoDB
import de.doubleslash.cmpprototype.data.datasource.local.localDB.dto.FileDTO
import de.doubleslash.cmpprototype.domain.repository.localStorage.FileStorageRepository

class FileStorageRepositoryImpl(
    private val mongoDB: MongoDB
) : FileStorageRepository {
    override suspend fun addFile(file: FileDTO) {
        mongoDB.addFile(file)
    }

    override fun getAllFiles(): List<FileDTO> {
        return mongoDB.getAllFiles()
    }

    override suspend fun deleteFile(file: FileDTO) {
        mongoDB.deleteFile(file)
    }

}