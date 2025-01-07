package de.doubleslash.cmpprototype.domain.repository.secureStore

/**
 * Handles secure data storage of credentials.
 */
interface SecureStoreRepository {

    fun saveSessionId(sessionId: String)
    fun getSessionId(): String?

    fun saveServerAddress(serverAddress: String)
    fun getServerAddress(): String?

    fun saveUsername(username: String)
    fun getUsername(): String?

    fun savePassword(password: String)
    fun getPassword(): String?

    fun saveUserId(userId: Int)
    fun getUserId(): Int?

    fun savePreviouslyAuthenticated()
    fun getPreviouslyAuthenticated(): Boolean?
}