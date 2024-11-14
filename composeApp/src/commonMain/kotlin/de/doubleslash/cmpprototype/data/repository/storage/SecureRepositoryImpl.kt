package de.doubleslash.cmpprototype.data.repository.storage

import de.doubleslash.cmpprototype.data.datasource.local.preferences.Preferences
import de.doubleslash.cmpprototype.domain.repository.storage.SecureRepository

/** Repository Implementation for managing secure storage of sensitive data. */
class SecureRepositoryImpl(
    private val encryptedSharedPreferences: Preferences
) : SecureRepository {
    override fun saveSessionId(sessionId: String) {
        encryptedSharedPreferences.saveString("sessionId", sessionId)
    }

    override fun getSessionId(): String {
        return encryptedSharedPreferences.getString("sessionId") ?: ""
    }

    override fun saveUserId(userId: Int) {
        encryptedSharedPreferences.saveInt("userId", userId)
    }

    override fun getUserId(): Int {
        return encryptedSharedPreferences.getInt("userId") ?: -1
    }
}