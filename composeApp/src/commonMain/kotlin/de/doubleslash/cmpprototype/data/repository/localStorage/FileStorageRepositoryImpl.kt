package de.doubleslash.cmpprototype.data.repository.localStorage

import de.doubleslash.cmpprototype.data.datasource.local.localDB.MongoDB
import de.doubleslash.cmpprototype.data.datasource.local.localDB.dto.FileDTO
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel
import de.doubleslash.cmpprototype.domain.repository.localStorage.FileStorageRepository
import org.mongodb.kbson.ObjectId

class FileStorageRepositoryImpl(
    private val mongoDB: MongoDB
) : FileStorageRepository {
    override suspend fun addFile(file: FileDTO) {
        mongoDB.addFile(file)
    }

    override fun getAllFiles(): List<FileDTO> {
        return mongoDB.getAllFiles()
    }

    override suspend fun deleteFile(file: FileDTO): Boolean {
        return mongoDB.deleteFile(file)
    }

    override fun getFileWithContent(id: ObjectId): FileModel {
        val fileDTO = mongoDB.getFileWithContent(id)
        return FileModel(
            _id = fileDTO._id,
            baseName = fileDTO.baseName,
            extension = fileDTO.extension,
            isRemoteFile = false,
            path = fileDTO.path,
            baseTypeId = fileDTO.baseTypeId,
            fileContent = fileDTO.fileContent // with byte array
        )
    }

}