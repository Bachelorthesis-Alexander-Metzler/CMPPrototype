package de.doubleslash.cmpprototype.domain.repository.cmis

import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel

interface CMISRepository {
    suspend fun fetchCMISObjects(
        serverAddress: String,
        username: String,
        password: String): RequestCondition<List<FileModel>>
}