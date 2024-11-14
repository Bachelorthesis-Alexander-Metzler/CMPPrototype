package de.doubleslash.cmpprototype.ui.presentation.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cmpprototype.composeapp.generated.resources.Res
import cmpprototype.composeapp.generated.resources.login_top_bar_title
import cmpprototype.composeapp.generated.resources.no_internet_connection
import de.doubleslash.cmpprototype.ui.presentation.screen.login.components.HandleAuthenticationState
import de.doubleslash.cmpprototype.ui.presentation.screen.login.components.LoginButton
import de.doubleslash.cmpprototype.ui.presentation.screen.login.components.PasswordField
import de.doubleslash.cmpprototype.ui.presentation.screen.login.components.PreviouslyAuthenticatedCheckbox
import de.doubleslash.cmpprototype.ui.presentation.screen.login.components.ServerAddressField
import de.doubleslash.cmpprototype.ui.presentation.screen.login.components.UsernameField
import io.github.alexzhirkevich.cupertino.CupertinoTopAppBarDefaults
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveTopAppBar
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.alexzhirkevich.cupertino.theme.CupertinoTheme
import org.jetbrains.compose.resources.stringResource



class LoginScreen : Screen{

    @OptIn(ExperimentalAdaptiveApi::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        // linear navigator to navigate to MainTabScreen after successful login
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<LoginViewModel>()
        var passwordVisible by remember { mutableStateOf(false) }

        val authState = viewModel.authState
        val isConnected by viewModel.isConnected.collectAsState()

        Scaffold(
            topBar = {
                AdaptiveTopAppBar(
                    title = { Text(text = stringResource(Res.string.login_top_bar_title))}
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding() + 15.dp, start = 15.dp, end = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Show message when no internet connection is available
                if (!isConnected) {
                    Snackbar(
                        modifier = Modifier.padding(bottom = 15.dp, top = 15.dp),
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                    ) {
                        Text(stringResource(Res.string.no_internet_connection))
                    }
                }

                // Server Address Field
                ServerAddressField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp),
                    serverAddress = viewModel.serverAddress,
                    enabled = isConnected,
                    onValueChange = { viewModel.serverAddress = it })

                // Username Field
                UsernameField(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp),
                    username = viewModel.username,
                    enabled = isConnected,
                    onValueChange = { viewModel.username = it })

                // Password Field
                PasswordField(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp),
                    password = viewModel.password,
                    enabled = isConnected,
                    onValueChange = { viewModel.password = it },
                    passwordVisible = passwordVisible,
                    onPasswordVisibilityChange = { passwordVisible = !passwordVisible }
                )

                // Checkbox for isPreviouslyAuthenticated (no requirement)
                PreviouslyAuthenticatedCheckbox(
                    Modifier.fillMaxWidth()
                        .height(56.dp)
                        .toggleable(
                            value = viewModel.isPreviouslyAuthenticated,
                            onValueChange = { viewModel.isPreviouslyAuthenticated = it },
                            role = Role.Checkbox
                        )
                        .padding(horizontal = 16.dp),
                    viewModel = viewModel)

                // Login Button
                LoginButton(
                    Modifier.padding(top = 15.dp),
                    onClick = { viewModel.onLoginClick() },
                    enabled = if (isConnected) {
                        viewModel.serverAddress.isNotBlank()
                                && viewModel.username.isNotBlank()
                                && viewModel.password.isNotBlank()
                    } else {
                        // login button enabled if no internet connection to allow login with local auth
                        true
                    }
                )

                // Authentication State
                HandleAuthenticationState(authState = authState, navigator = navigator, isConnected = isConnected)
            }
        }
    }
}

