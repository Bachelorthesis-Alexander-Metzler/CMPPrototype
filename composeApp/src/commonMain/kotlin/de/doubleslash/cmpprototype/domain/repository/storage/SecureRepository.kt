package de.doubleslash.cmpprototype.domain.repository.storage

/** Repository for managing secure storage of sensitive data. */
interface SecureRepository {
    fun saveSessionId(sessionId: String)
    fun getSessionId(): String

    fun saveUserId(userId: Int)
    fun getUserId(): Int
}