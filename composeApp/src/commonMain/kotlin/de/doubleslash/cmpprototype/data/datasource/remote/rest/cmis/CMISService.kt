package de.doubleslash.cmpprototype.data.datasource.remote.rest.cmis

import de.doubleslash.cmpprototype.data.datasource.remote.rest.cmis.dto.CMISObjectDTO
import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition

/**
 * Provides CMIS-based API calls for file operations, including upload and download.
 * Works with a CMIS server to enable file handling features.
 */
interface CMISService {
    suspend fun fetchCMISObjects(
        serverAddress: String,
        username: String,
        password: String): RequestCondition<List<CMISObjectDTO>>
}
