package de.doubleslash.cmpprototype.data.repository.auth

import de.doubleslash.cmpprototype.data.datasource.local.preferences.Preferences
import de.doubleslash.cmpprototype.data.datasource.remote.rest.auth.AuthApi
import de.doubleslash.cmpprototype.data.datasource.remote.rest.auth.dto.LoginDTO
import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition
import de.doubleslash.cmpprototype.domain.repository.auth.AuthRepository

class AuthRepositoryImpl(
    private val api: AuthApi, // dependency injection
    private val encryptedSharedPreferences: Preferences
) : AuthRepository {

    override suspend fun authenticateUser(
        serverAddress: String,
        username: String,
        password: String
    ): RequestCondition<LoginDTO> {
        return api.authenticateUser(serverAddress, username, password)
    }

    override fun saveSessionId(sessionId: String) {
        encryptedSharedPreferences.saveString("sessionId", sessionId)
    }

    override fun getSessionId(): String? {
        return encryptedSharedPreferences.getString("sessionId")
    }

    override fun saveUserId(userId: Int) {
        encryptedSharedPreferences.saveInt("userId", userId)
    }

    override fun getUserId(): Int? {
        return encryptedSharedPreferences.getInt("userId")
    }
}