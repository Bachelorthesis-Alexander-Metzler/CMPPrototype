package de.doubleslash.cmpprototype.ui.presentation.screen.login.components

import androidx.compose.foundation.layout.padding
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
import de.doubleslash.cmpprototype.common.SessionManager
import de.doubleslash.cmpprototype.domain.model.auth.LoginModel
import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.BottomTabManager
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveCircularProgressIndicator
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun HandleAuthenticationState(
    authState: RequestCondition<LoginModel>,
    navigator: Navigator,
    isConnected: Boolean
) {
    when (authState) {
        is RequestCondition.IdleCondition -> {}
        is RequestCondition.LoadingCondition -> {
            AdaptiveCircularProgressIndicator(modifier = Modifier.padding(top = 15.dp))
            Text(
                modifier = Modifier.padding(top = 15.dp),
                text = stringResource(Res.string.loading_text)
            )
        }

        is RequestCondition.ErrorCondition -> {
            Text(
                modifier = Modifier.padding(top = 15.dp),
                text = stringResource(Res.string.login_failed_text) + ": " + authState.getErrorMessage()
            )
        }

        is RequestCondition.SuccessCondition -> {
            Text(
                modifier = Modifier.padding(top = 15.dp),
                text = stringResource(Res.string.login_success_text)
            )

            // Navigate on success using LaunchedEffect to ensure it only happens once
            LaunchedEffect(Unit) {
                SessionManager.login()

                if (!isConnected) {
                    // Navigate to DownloadScreen when offline
                    navigator.push(BottomTabManager(navigateToDownloadScreen = true))
                } else {
                    // Default navigation to the BottomTabManager (FileScreen by default)
                    navigator.push(BottomTabManager(navigateToDownloadScreen = false))
                }
            }
        }
    }
}