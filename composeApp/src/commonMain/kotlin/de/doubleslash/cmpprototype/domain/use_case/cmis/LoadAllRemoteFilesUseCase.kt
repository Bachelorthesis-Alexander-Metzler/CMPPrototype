package de.doubleslash.cmpprototype.domain.use_case.cmis

import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel
import de.doubleslash.cmpprototype.domain.services.rest.cmis.CMISService
import de.doubleslash.cmpprototype.domain.services.rest.cmis.dto.CMISObjectDTO

class LoadAllRemoteFilesUseCase(
    private val cmisService: CMISService
) {
    suspend operator fun invoke(
        serverAddress: String,
        username: String,
        password: String
    ): RequestCondition<List<FileModel>> {
        return fetchCMISFiles(serverAddress, username, password)
    }

    private suspend fun fetchCMISFiles(
        serverAddress: String,
        username: String,
        password: String
    ): RequestCondition<List<FileModel>> {
        // fetches CMIS files from CMIS Browser Binding REST API
        val requestCondition = cmisService.fetchCMISFiles(serverAddress, username, password)

        return mapToFileModels(requestCondition)
    }

    /**
     * Maps the RequestCondition from CMISService to a domain-specific RequestCondition with FileModel.
     */
    private fun mapToFileModels(
        requestCondition: RequestCondition<List<CMISObjectDTO>>
    ): RequestCondition<List<FileModel>> {
        return when (requestCondition) {
            is RequestCondition.SuccessCondition -> {
                println("Successfully fetched CMIS files")
                val fileModels = requestCondition.data.map {
                    FileModel(
                        baseName = it.name,
                        extension = it.name.substringAfterLast('.', ""),
                        baseTypeId = it.baseTypeId,
                        isRemoteFile = true
                    )
                }
                RequestCondition.SuccessCondition(data = fileModels)
            }

            is RequestCondition.ErrorCondition -> {
                println("Error fetching CMIS files")
                RequestCondition.ErrorCondition(errorMsg = requestCondition.errorMsg)
            }

            is RequestCondition.IdleCondition -> RequestCondition.IdleCondition
            is RequestCondition.LoadingCondition -> RequestCondition.LoadingCondition
        }
    }
}