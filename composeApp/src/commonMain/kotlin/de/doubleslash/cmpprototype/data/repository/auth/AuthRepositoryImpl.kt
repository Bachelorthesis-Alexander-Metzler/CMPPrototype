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
        val requestCondition = api.authenticateUser(serverAddress, username, password)

        when (requestCondition) {
            is RequestCondition.SuccessCondition -> {
                // store session data and credentials if authentication was successful
                saveServerAddress(serverAddress)
                saveUsername(username)
                savePassword(password)
                saveSessionId(requestCondition.data.sessionId)
                saveUserId(requestCondition.data.userId)
                savePreviouslyAuthenticated()
            }

            is RequestCondition.ErrorCondition -> { /* nothing to do */ }
            is RequestCondition.IdleCondition -> { /* nothing to do */ }
            is RequestCondition.LoadingCondition -> { /* nothing to do */ }
        }

        return requestCondition
    }

    override fun saveSessionId(sessionId: String) {
        encryptedSharedPreferences.saveString("sessionId", sessionId)
    }

    override fun getSessionId(): String? {
        return encryptedSharedPreferences.getString("sessionId")
    }

    override fun saveServerAddress(serverAddress: String) {
        encryptedSharedPreferences.saveString("serverAddress", serverAddress)
    }

    override fun getServerAddress(): String? {
        return encryptedSharedPreferences.getString("serverAddress")
    }

    override fun saveUsername(username: String) {
        encryptedSharedPreferences.saveString("username", username)
    }

    override fun getUsername(): String? {
        return encryptedSharedPreferences.getString("username")
    }

    override fun savePassword(password: String) {
        encryptedSharedPreferences.saveString("password", password)
    }

    override fun getPassword(): String? {
        return encryptedSharedPreferences.getString("password")
    }

    override fun saveUserId(userId: Int) {
        encryptedSharedPreferences.saveInt("userId", userId)
    }

    override fun getUserId(): Int? {
        return encryptedSharedPreferences.getInt("userId")
    }

    override fun savePreviouslyAuthenticated() {
        encryptedSharedPreferences.saveBoolean("previouslyAuthenticated", true)
    }

    override fun getPreviouslyAuthenticated(): Boolean? {
        return encryptedSharedPreferences.getBoolean("previouslyAuthenticated")
    }
}