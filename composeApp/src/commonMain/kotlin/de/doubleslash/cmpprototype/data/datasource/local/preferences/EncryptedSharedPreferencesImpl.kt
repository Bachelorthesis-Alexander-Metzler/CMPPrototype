package de.doubleslash.cmpprototype.data.datasource.local.preferences

import com.russhwolf.settings.Settings

class EncryptedSharedPreferencesImpl(
    private val settings: Settings // dependency injection
) : Preferences {

    override fun saveString(key: String, value: String) {
        settings.putString(key, value)
    }

    override fun getString(key: String): String? {
        return settings.getStringOrNull(key)
    }

    override fun saveInt(key: String, value: Int) {
        settings.putInt(key, value)
    }

    override fun getInt(key: String): Int? {
        return settings.getIntOrNull(key)
    }
}