package de.doubleslash.cmpprototype.domain.use_case.getSessionData

import de.doubleslash.cmpprototype.domain.model.auth.CredentialsModel
import de.doubleslash.cmpprototype.domain.repository.secureStore.SecureStoreRepository

class GetCredentialsUseCase(
    private val secureStoreRepository: SecureStoreRepository
) {
    operator fun invoke(): CredentialsModel {
        val serverAddress = secureStoreRepository.getServerAddress() ?: throw IllegalStateException(
            "Server address not found"
        )
        val username =
            secureStoreRepository.getUsername() ?: throw IllegalStateException("Username not found")
        val password =
            secureStoreRepository.getPassword() ?: throw IllegalStateException("Password not found")

        return CredentialsModel(serverAddress, username, password)
    }
}