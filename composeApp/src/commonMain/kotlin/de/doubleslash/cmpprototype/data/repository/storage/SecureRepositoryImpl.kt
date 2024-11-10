package de.doubleslash.cmpprototype.data.repository.storage

import de.doubleslash.cmpprototype.domain.repository.storage.SecureRepository

/** Repository Implementation for managing secure storage of sensitive data. */
class SecureRepositoryImpl(
    // TODO: SecureStorage
) : SecureRepository {
    override fun saveSessionId(sessionId: String) {
        TODO("Not yet implemented")
    }

    override fun getSessionId(): String {
        TODO("Not yet implemented")
    }

    override fun saveUserId(userId: String) {
        TODO("Not yet implemented")
    }

    override fun getUserId(): String {
        TODO("Not yet implemented")
    }
}