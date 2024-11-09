package de.doubleslash.cmpprototype.ui.presentation.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cmpprototype.composeapp.generated.resources.Res
import cmpprototype.composeapp.generated.resources.login_top_bar_title
import de.doubleslash.cmpprototype.ui.presentation.screen.login.components.AuthenticationState
import de.doubleslash.cmpprototype.ui.presentation.screen.login.components.LoginButton
import de.doubleslash.cmpprototype.ui.presentation.screen.login.components.PasswordField
import de.doubleslash.cmpprototype.ui.presentation.screen.login.components.ServerAddressField
import de.doubleslash.cmpprototype.ui.presentation.screen.login.components.UsernameField
import org.jetbrains.compose.resources.stringResource



class LoginScreen : Screen{

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        // linear navigator to navigate to MainTabScreen after successful login
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<LoginViewModel>()
        var passwordVisible by remember { mutableStateOf(false) }

        val authState = viewModel.authState

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = stringResource(Res.string.login_top_bar_title))},
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    ),
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
                // Server Address Field
                ServerAddressField(serverAddress = viewModel.serverAddress, onValueChange = { viewModel.serverAddress = it })

                // Username Field
                UsernameField(username = viewModel.username, onValueChange = { viewModel.username = it })

                // Password Field
                PasswordField(
                    password = viewModel.password,
                    onValueChange = { viewModel.password = it },
                    passwordVisible = passwordVisible,
                    onPasswordVisibilityChange = { passwordVisible = !passwordVisible }
                )

                // Login Button
                LoginButton(
                    onClick = { viewModel.authenticateUser() },
                    enabled = viewModel.serverAddress.isNotBlank()
                            && viewModel.username.isNotBlank()
                            && viewModel.password.isNotBlank())

                // Authentication State
                AuthenticationState(authState = authState, navigator = navigator)
            }
        }
    }

}

