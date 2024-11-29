package de.doubleslash.cmpprototype.domain.use_case.cmis

import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition
import de.doubleslash.cmpprototype.domain.model.file_mgmt.FolderModel
import de.doubleslash.cmpprototype.domain.repository.cmis.CMISRepository

class LoadAllRemoteFoldersUseCase(
    private val cmisRepository: CMISRepository
) {
    suspend operator fun invoke(
        serverAddress: String,
        username: String,
        password: String
    ) : RequestCondition<List<FolderModel>> {
        return cmisRepository.fetchCMISFolders(serverAddress, username, password)
    }
}