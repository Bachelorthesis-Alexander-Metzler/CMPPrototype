package de.doubleslash.cmpprototype.domain.use_case.getPreviouslyAuthenticated

import de.doubleslash.cmpprototype.domain.repository.secureStore.SecureStoreRepository

class GetPreviouslyAuthenticatedUseCase(
    private val secureStoreRepository: SecureStoreRepository
) {

    operator fun invoke(): Boolean {
        return secureStoreRepository.getPreviouslyAuthenticated() ?: false
    }
}