package de.doubleslash.cmpprototype.domain.repository.cmis

import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FolderModel

interface CMISRepository {
    suspend fun fetchCMISFiles(
        serverAddress: String,
        username: String,
        password: String
    ): RequestCondition<List<FileModel>>

    suspend fun fetchCMISFolders(
        serverAddress: String,
        username: String,
        password: String
    ): RequestCondition<List<FolderModel>>
}