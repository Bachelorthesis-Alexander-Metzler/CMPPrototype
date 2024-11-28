package de.doubleslash.cmpprototype.domain.use_case.getSessionData

import de.doubleslash.cmpprototype.domain.model.auth.CredentialsModel
import de.doubleslash.cmpprototype.domain.repository.auth.AuthRepository

class GetCredentialsUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): CredentialsModel {
        val serverAddress = authRepository.getServerAddress() ?: throw IllegalStateException("Server address not found")
        val username = authRepository.getUsername() ?: throw IllegalStateException("Username not found")
        val password = authRepository.getPassword() ?: throw IllegalStateException("Password not found")

        return CredentialsModel(serverAddress, username, password)
    }
}