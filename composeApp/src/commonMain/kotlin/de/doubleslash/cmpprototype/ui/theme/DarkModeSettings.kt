package de.doubleslash.cmpprototype.ui.theme

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * A CompositionLocal that provides access to the global DarkModeSettings state.
 * This allows any Composable in the Composition hierarchy to access and update
 * dark mode preferences such as whether to follow system settings or a manual mode.
 */
val LocalDarkModeSettings = staticCompositionLocalOf { mutableStateOf(DarkModeSettings()) }

/**
 * Data class representing the dark mode settings of the application.
 *
 * @property useSystemSettings When true, the app theme will follow the system's dark mode setting.
 *                             When false, dark mode will follow the value in [isDarkModeEnabled].
 * @property isDarkModeEnabled Controls the dark mode state when [useSystemSettings] is false.
 *                             If true, dark mode is enabled; if false, light mode is used.
 */
data class DarkModeSettings(
    var useSystemSettings: Boolean = true,
    var isDarkModeEnabled: Boolean = false
)
