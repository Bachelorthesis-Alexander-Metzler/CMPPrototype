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
        // TODO: get session id from SecureStorage
        return "sessionId"
    }

    override fun saveUserId(userId: Int) {
        TODO("Not yet implemented")
    }

    override fun getUserId(): Int {
        // TODO: get user id from SecureStorage
        return 0
    }
}