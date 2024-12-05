package de.doubleslash.cmpprototype

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.ScaleTransition
import cmpprototype.composeapp.generated.resources.Res
import cmpprototype.composeapp.generated.resources.confirm
import cmpprototype.composeapp.generated.resources.file_saved_please_login
import cmpprototype.composeapp.generated.resources.login_required
import de.doubleslash.cmpprototype.common.SessionManager
import de.doubleslash.cmpprototype.ui.presentation.screen.login.LoginScreen
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.BottomTabManager
import de.doubleslash.cmpprototype.ui.theme.AppTheme
import de.doubleslash.cmpprototype.ui.theme.DarkModeSettings
import de.doubleslash.cmpprototype.ui.theme.LocalDarkModeSettings
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveAlertDialog
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.alexzhirkevich.cupertino.default
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
@Preview
fun App(processedIntent: Boolean = false) {
    // DarkModeSettings für die App
    val darkModeSettings = remember { mutableStateOf(DarkModeSettings()) }
    val showLoginAlertDialog = remember { mutableStateOf(false) }

    // check login status and intent processing to determine if alert dialog should be shown
    LaunchedEffect(processedIntent, SessionManager.isUserLoggedIn.value) {
        if (processedIntent) {
            if (!SessionManager.isUserLoggedIn.value) {
                showLoginAlertDialog.value = true
            }
        }
    }

    CompositionLocalProvider(LocalDarkModeSettings provides darkModeSettings) {
        AppTheme {
            if (!SessionManager.isUserLoggedIn.value
                && !processedIntent
            ) {
                // user is not logged in and no intent is processed -> show login screen
                Navigator(LoginScreen()) { navigator ->
                    ScaleTransition(navigator = navigator)
                }
            } else if (!SessionManager.isUserLoggedIn.value
                && processedIntent
            ) {
                // user is not logged in and intent is processed -> show login screen and alert dialog
                Navigator(LoginScreen()) { navigator ->
                    ScaleTransition(navigator = navigator)
                }

                if (showLoginAlertDialog.value) {
                    AdaptiveAlertDialog(
                        onDismissRequest = { showLoginAlertDialog.value = false },
                        message = { Text(stringResource(Res.string.file_saved_please_login)) },
                        title = { Text(stringResource(Res.string.login_required)) },
                        buttons = {
                            default(
                                onClick = { showLoginAlertDialog.value = false },
                                title = {
                                    Text(stringResource(Res.string.confirm))
                                }
                            )
                        }
                    )
                }
            } else if (SessionManager.isUserLoggedIn.value
                && !processedIntent
            ) {
                // user is logged in and no intent is processed -> navigate to FileScreen
                Navigator(BottomTabManager(navigateToDownloadScreen = false)) { navigator ->
                    ScaleTransition(navigator = navigator)
                }
            } else {
                // user is logged in and intent is processed -> navigate to DownloadScreen
                Navigator(BottomTabManager(navigateToDownloadScreen = true)) { navigator ->
                    ScaleTransition(navigator = navigator)
                }
            }
        }
    }
}
