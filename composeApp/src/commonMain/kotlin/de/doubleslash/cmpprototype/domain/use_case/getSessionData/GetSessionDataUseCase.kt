package de.doubleslash.cmpprototype.domain.use_case.getSessionData

import de.doubleslash.cmpprototype.domain.model.auth.LoginModel
import de.doubleslash.cmpprototype.domain.repository.secureStore.SecureStoreRepository

/**
 * Use case for getting session data from local storage.
 */
class GetSessionDataUseCase(
    private val secureStoreRepository: SecureStoreRepository
) {

    operator fun invoke(): LoginModel {
        val sessionId = secureStoreRepository.getSessionId()
            ?: throw IllegalStateException("Session ID not found")
        val userId =
            secureStoreRepository.getUserId() ?: throw IllegalStateException("User ID not found")

        return LoginModel(sessionId, userId)
    }
}