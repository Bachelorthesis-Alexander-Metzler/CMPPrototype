package de.doubleslash.cmpprototype.data.repository.cmis

import de.doubleslash.cmpprototype.data.datasource.remote.rest.cmis.CMISService
import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel
import de.doubleslash.cmpprototype.domain.repository.cmis.CMISRepository

class CMISRepositoryImpl(
    private val cmisService: CMISService
) : CMISRepository {
    override suspend fun fetchCMISObjects(
        serverAddress: String,
        username: String,
        password: String
    ): RequestCondition<List<FileModel>> {
        val requestCondition = cmisService.fetchCMISObjects(serverAddress, username, password)

        when (requestCondition) {
            is RequestCondition.SuccessCondition -> {
                println("Successfully fetched CMIS objects")
                val fileModels = requestCondition.data.map {
                    FileModel(
                        baseName = it.name,
                        extension = it.name.substringAfterLast('.'),
                        baseTypeId = it.baseTypeId,
                        isRemoteFile = true
                    )
                }
                return RequestCondition.SuccessCondition(data = fileModels)
            }
            is RequestCondition.ErrorCondition -> {
                println("Error fetching CMIS objects")
                return RequestCondition.ErrorCondition(errorMsg = "Error fetching CMIS objects")
            }

            is RequestCondition.IdleCondition -> { return RequestCondition.IdleCondition }
            is RequestCondition.LoadingCondition -> { return RequestCondition.LoadingCondition }
        }
    }

}