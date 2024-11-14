package de.doubleslash.cmpprototype.domain.use_case.getSessionData

import de.doubleslash.cmpprototype.domain.model.auth.LoginModel
import de.doubleslash.cmpprototype.domain.repository.auth.AuthRepository

/**
 * Use case for getting session data from local storage.
 */
class GetSessionDataUseCase(
    private val authRepository: AuthRepository
) {

    operator fun invoke(): LoginModel {
        val sessionId = authRepository.getSessionId() ?: throw IllegalStateException("Session ID not found")
        val userId = authRepository.getUserId() ?: throw IllegalStateException("User ID not found")

        return LoginModel(sessionId, userId)
    }
}