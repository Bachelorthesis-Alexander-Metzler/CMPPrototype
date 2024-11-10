package de.doubleslash.cmpprototype.ui.presentation.screen.login.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import cmpprototype.composeapp.generated.resources.Res
import cmpprototype.composeapp.generated.resources.loading_text
import cmpprototype.composeapp.generated.resources.login_failed_text
import cmpprototype.composeapp.generated.resources.login_success_text
import de.doubleslash.cmpprototype.domain.model.auth.LoginModel
import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.BottomTabManager
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.download.DownloadScreen
import org.jetbrains.compose.resources.stringResource

@Composable
fun HandleAuthenticationState(authState: RequestCondition<LoginModel>, navigator: Navigator, isConnected: Boolean) {
    when (authState) {
        is RequestCondition.IdleCondition -> {}
        is RequestCondition.LoadingCondition -> {
            CircularProgressIndicator()
            Text(modifier = Modifier.padding(top = 15.dp), text = stringResource(Res.string.loading_text))
        }
        is RequestCondition.ErrorCondition -> {
            Text(modifier = Modifier.padding(top = 15.dp), text = stringResource(Res.string.login_failed_text) + ": " + authState.getErrorMessage())
        }
        is RequestCondition.SuccessCondition -> {
            Text(modifier = Modifier.padding(top = 15.dp), text = stringResource(Res.string.login_success_text))

            // Navigate on success using LaunchedEffect to ensure it only happens once
            LaunchedEffect(Unit) {
                if (!isConnected) {
                    // Navigate to DownloadScreen when offline (tab bar is not visible, only download screen)
                    navigator.push(DownloadScreen())
                } else {
                    // Default navigation to the BottomTabManager (FileScreen by default)
                    navigator.push(BottomTabManager())
                }
            }
        }
    }
}