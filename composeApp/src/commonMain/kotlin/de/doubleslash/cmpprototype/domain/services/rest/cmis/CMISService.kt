package de.doubleslash.cmpprototype.domain.services.rest.cmis

import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition
import de.doubleslash.cmpprototype.domain.services.rest.cmis.dto.CMISObjectDTO

/**
 * Provides CMIS-based API calls for file operations, including upload and download.
 * Works with a CMIS server to enable file handling features.
 */
interface CMISService {
    suspend fun fetchCMISFiles(
        serverAddress: String,
        username: String,
        password: String
    ): RequestCondition<List<CMISObjectDTO>>

    suspend fun fetchCMISFolders(
        serverAddress: String,
        username: String,
        password: String
    ): RequestCondition<List<CMISObjectDTO>>

}
