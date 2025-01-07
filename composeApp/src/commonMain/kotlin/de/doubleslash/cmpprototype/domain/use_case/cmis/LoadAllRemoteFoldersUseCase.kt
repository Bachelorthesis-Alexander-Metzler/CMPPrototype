package de.doubleslash.cmpprototype.domain.use_case.cmis

import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FolderModel
import de.doubleslash.cmpprototype.domain.services.rest.cmis.CMISService
import de.doubleslash.cmpprototype.domain.services.rest.cmis.dto.CMISObjectDTO

class LoadAllRemoteFoldersUseCase(
    private val cmisService: CMISService
) {
    suspend operator fun invoke(
        serverAddress: String,
        username: String,
        password: String
    ): RequestCondition<List<FolderModel>> {
        return fetchCMISFolders(serverAddress, username, password)
    }

    private suspend fun fetchCMISFolders(
        serverAddress: String,
        username: String,
        password: String
    ): RequestCondition<List<FolderModel>> {
        // fetches CMIS folders from CMIS Browser Binding REST API
        val requestCondition = cmisService.fetchCMISFolders(serverAddress, username, password)

        return mapToFolderModels(requestCondition)
    }

    /**
     * Maps the RequestCondition from CMISService to a domain-specific RequestCondition with FolderModel.
     */
    private fun mapToFolderModels(
        requestCondition: RequestCondition<List<CMISObjectDTO>>
    ): RequestCondition<List<FolderModel>> {
        return when (requestCondition) {
            is RequestCondition.SuccessCondition -> {
                println("Successfully fetched CMIS folders")
                val folderModels = requestCondition.data.map {
                    FolderModel(
                        name = it.name,
                        baseTypeId = it.baseTypeId
                    )
                }
                RequestCondition.SuccessCondition(data = folderModels)
            }

            is RequestCondition.ErrorCondition -> {
                println("Error fetching CMIS folders")
                RequestCondition.ErrorCondition(errorMsg = requestCondition.errorMsg)
            }

            is RequestCondition.IdleCondition -> RequestCondition.IdleCondition
            is RequestCondition.LoadingCondition -> RequestCondition.LoadingCondition
        }
    }
}