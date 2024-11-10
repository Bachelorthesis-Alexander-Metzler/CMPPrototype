package de.doubleslash.cmpprototype.domain.use_case.getSessionData

import de.doubleslash.cmpprototype.domain.model.auth.LoginModel
import de.doubleslash.cmpprototype.domain.repository.storage.SecureRepository

/**
 * Use case for getting session data from local storage.
 */
class GetSessionDataUseCase(
    private val secureRepository: SecureRepository
) {

    operator fun invoke(): LoginModel {
        val sessionId = secureRepository.getSessionId()
        val userId = secureRepository.getUserId()

        return LoginModel(sessionId, userId)
    }
}