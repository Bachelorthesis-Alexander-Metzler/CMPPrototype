package de.doubleslash.cmpprototype.data.repository.secureStore

import de.doubleslash.cmpprototype.data.datasource.local.preferences.Preferences
import de.doubleslash.cmpprototype.domain.repository.secureStore.SecureStoreRepository

class SecureStoreRepositoryImpl(
    private val encryptedSharedPreferences: Preferences
) : SecureStoreRepository {

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