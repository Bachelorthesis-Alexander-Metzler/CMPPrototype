package de.doubleslash.cmpprototype.data.datasource.local.preferences

import com.russhwolf.settings.Settings

class SharedPreferencesImpl(
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

    override fun saveBoolean(key: String, value: Boolean) {
        settings.putBoolean(key, value)
    }

    override fun getBoolean(key: String): Boolean? {
        return settings.getBooleanOrNull(key)
    }
}