package de.doubleslash.cmpprototype.domain.repository.auth

import de.doubleslash.cmpprototype.data.datasource.remote.rest.auth.dto.LoginDTO
import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition

/**
 * Repository for managing user authentication, combining AuthService and AuthLocalDataSource.
 * Handles session creation, renewal, and local storage of credentials.
 */
interface AuthRepository {
    suspend fun authenticateUser(
        serverAddress: String,
        username: String,
        password: String): RequestCondition<LoginDTO>

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