package de.doubleslash.cmpprototype.data.datasource.local.preferences

import com.russhwolf.settings.Settings

expect fun provideSharedPreferences(): Settings
expect fun provideEncryptedSharedPreferences(): Settings

/* interface for shared preferences implementations */
interface Preferences {
    fun saveString(key: String, value: String)
    fun getString(key: String): String?
    fun saveInt(key: String, value: Int)
    fun getInt(key: String): Int?
}