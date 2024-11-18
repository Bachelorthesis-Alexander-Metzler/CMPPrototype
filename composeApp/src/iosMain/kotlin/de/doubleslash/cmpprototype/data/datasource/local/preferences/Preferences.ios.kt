package de.doubleslash.cmpprototype.data.datasource.local.preferences

import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import de.doubleslash.cmpprototype.common.Constants
import platform.Foundation.NSUserDefaults

/** provides Apple Settings NSUserDefaults */
actual fun provideSharedPreferences(): Settings {
    val userDefaults: NSUserDefaults = NSUserDefaults.standardUserDefaults
    return NSUserDefaultsSettings(userDefaults)
}

/** provides Apple Settings Keychain */
@OptIn(ExperimentalSettingsImplementation::class)
actual fun provideEncryptedSharedPreferences(): Settings {
    val serviceName = Constants.KEYCHAIN_SERVICE_NAME
    return KeychainSettings(serviceName)
}