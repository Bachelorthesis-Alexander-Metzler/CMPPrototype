package de.doubleslash.cmpprototype.ui.presentation.screen.tabs.settings.details.theme_details

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cmpprototype.composeapp.generated.resources.Res
import cmpprototype.composeapp.generated.resources.content_description_back
import cmpprototype.composeapp.generated.resources.setting_enable_dark_mode
import cmpprototype.composeapp.generated.resources.setting_use_system_theme
import cmpprototype.composeapp.generated.resources.theme_details_title
import de.doubleslash.cmpprototype.ui.theme.LocalDarkModeSettings
import de.doubleslash.cmpprototype.ui.theme.darkSchemeState
import de.doubleslash.cmpprototype.ui.theme.lightSchemeState
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveButton
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveHorizontalDivider
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveIconButton
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveSwitch
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveTopAppBar
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.alexzhirkevich.cupertino.adaptive.icons.AdaptiveIcons
import io.github.alexzhirkevich.cupertino.adaptive.icons.KeyboardArrowLeft
import org.jetbrains.compose.resources.stringResource

class ThemeDetailsScreen : Screen {
    @OptIn(ExperimentalAdaptiveApi::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val darkModeSettingsState = LocalDarkModeSettings.current // access global state
        val darkModeSettings = darkModeSettingsState.value
        val lightScheme = lightSchemeState
        val darkScheme = darkSchemeState
        var colorInput by remember { mutableStateOf(TextFieldValue("")) }
        var isValidColor by remember { mutableStateOf(true) }

        Scaffold(
            topBar = {
                AdaptiveTopAppBar(
                    title = { Text(stringResource(Res.string.theme_details_title)) },
                    navigationIcon = {
                        AdaptiveIconButton(
                            onClick = { navigator.pop() },
                            content = {
                                Icon(
                                    imageVector = AdaptiveIcons.Outlined.KeyboardArrowLeft,
                                    contentDescription = stringResource(Res.string.content_description_back)
                                )
                            }
                        )
                    },
                    adaptation = {
                        material {
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            },

            ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(Res.string.setting_use_system_theme)
                        )
                        AdaptiveSwitch(
                            checked = darkModeSettings.useSystemSettings,
                            onCheckedChange = { checked ->
                                darkModeSettingsState.value = darkModeSettings.copy(
                                    useSystemSettings = checked,
                                    isDarkModeEnabled = if (checked) false else darkModeSettings.isDarkModeEnabled
                                )
                            }
                        )
                    }
                    AdaptiveHorizontalDivider()
                }

                // dark mode setting
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(Res.string.setting_enable_dark_mode)
                        )
                        AdaptiveSwitch(
                            checked = darkModeSettings.isDarkModeEnabled,
                            onCheckedChange = { checked ->
                                darkModeSettingsState.value = darkModeSettings.copy(
                                    isDarkModeEnabled = checked
                                )
                            },
                            enabled = !darkModeSettings.useSystemSettings
                        )
                    }
                    AdaptiveHorizontalDivider()
                }

                // Color picker setting for changing primary color
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = colorInput,
                            onValueChange = {
                                colorInput = it
                                isValidColor = parseColor(it.text) != null
                            },
                            label = { Text("Enter a color (e.g., #FF5733)") },
                            isError = !isValidColor,
                            modifier = Modifier.weight(1f),
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        AdaptiveButton(
                            onClick = {
                                val newColor = parseColor(colorInput.text)
                                if (newColor != null) {
                                    lightScheme.value = lightScheme.value.copy(primary = newColor)
                                    darkScheme.value = darkScheme.value.copy(primary = newColor)
                                }
                            },
                            enabled = isValidColor
                        ) {
                            Text("Change")
                        }

                    }
                    AdaptiveHorizontalDivider()
                }
            }
        }
    }

    /**
     * Parses a hex color string and returns a [Color] object, or null if invalid.
     */
    private fun parseColor(hex: String): Color? {
        return try {
            // Ensure the input starts with '#' and is of valid length
            if (!hex.startsWith("#") || (hex.length != 7 && hex.length != 9)) {
                null
            } else {
                val color = hex.substring(1).toLong(16)
                if (hex.length == 7) {
                    // Add full alpha if not specified
                    Color(color or 0xFF000000)
                } else {
                    // ARGB format
                    Color(color)
                }
            }
        } catch (e: Exception) {
            null
        }
    }
}
