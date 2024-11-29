package de.doubleslash.cmpprototype.data.repository.cmis

import de.doubleslash.cmpprototype.data.datasource.remote.rest.cmis.CMISService
import de.doubleslash.cmpprototype.data.datasource.remote.rest.cmis.dto.CMISObjectDTO
import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FileModel
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FolderModel
import de.doubleslash.cmpprototype.domain.repository.cmis.CMISRepository

class CMISRepositoryImpl(
    private val cmisService: CMISService
) : CMISRepository {

    override suspend fun fetchCMISFiles(
        serverAddress: String,
        username: String,
        password: String
    ): RequestCondition<List<FileModel>> {
        val requestCondition = cmisService.fetchCMISFiles(serverAddress, username, password)

        return mapToFileModels(requestCondition)
    }

    override suspend fun fetchCMISFolders(
        serverAddress: String,
        username: String,
        password: String
    ): RequestCondition<List<FolderModel>> {
        val requestCondition = cmisService.fetchCMISFolders(serverAddress, username, password)

        return mapToFolderModels(requestCondition)
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
