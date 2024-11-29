package de.doubleslash.cmpprototype.domain.use_case.localStorage

import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel
import de.doubleslash.cmpprototype.domain.repository.localStorage.FileStorageRepository
import org.mongodb.kbson.ObjectId

class LoadLocalFileUseCase(
    private val fileStorageRepository: FileStorageRepository
) {

    operator fun invoke(id: ObjectId): FileModel {
        return fileStorageRepository.getFileWithContent(id)
    }
}