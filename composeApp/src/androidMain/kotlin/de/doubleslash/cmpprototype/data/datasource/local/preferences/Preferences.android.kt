package de.doubleslash.cmpprototype.data.datasource.local.preferences

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import de.doubleslash.cmpprototype.common.Constants

lateinit var appContext: Context

actual fun provideSharedPreferences(): Settings {
    val sharedPreferences = appContext.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    return SharedPreferencesSettings(sharedPreferences)
}

actual fun provideEncryptedSharedPreferences(): Settings {
    val masterKey = MasterKey.Builder(appContext)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    val sharedPreferences = EncryptedSharedPreferences.create(
        appContext,
        Constants.SECURED_PREFS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    return SharedPreferencesSettings(sharedPreferences)
}