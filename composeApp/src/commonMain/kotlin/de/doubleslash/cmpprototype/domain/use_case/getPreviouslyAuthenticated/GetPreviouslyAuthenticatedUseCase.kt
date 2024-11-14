package de.doubleslash.cmpprototype.domain.use_case.getPreviouslyAuthenticated

import de.doubleslash.cmpprototype.domain.model.auth.LoginModel
import de.doubleslash.cmpprototype.domain.repository.auth.AuthRepository

class GetPreviouslyAuthenticatedUseCase(
    private val authRepository: AuthRepository
) {

    operator fun invoke(): Boolean {
        return authRepository.getPreviouslyAuthenticated() ?: false
    }
}