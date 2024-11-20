package de.doubleslash.cmpprototype.domain.use_case.localStorage

import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel
import de.doubleslash.cmpprototype.domain.repository.localStorage.FileStorageRepository

class LoadAllFilesUseCase(
    private val fileStorageRepository: FileStorageRepository
) {
    operator fun invoke(): List<FileModel> {
        return fileStorageRepository.getAllFiles().map {
            FileModel(
                _id = it._id,
                baseName = it.baseName,
                extension = it.extension,
                path = it.path
            )
        }
    }
}