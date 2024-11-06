package de.doubleslash.cmpprototype.ui.presentation.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cmpprototype.composeapp.generated.resources.Res
import cmpprototype.composeapp.generated.resources.ic_visibility_off
import cmpprototype.composeapp.generated.resources.ic_visibility_on
import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition
import org.jetbrains.compose.resources.vectorResource


class LoginScreen : Screen{

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<LoginViewModel>()
        var passwordVisible by remember { mutableStateOf(false) }

        // observe authentication state
        val authState = viewModel.authState

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Login") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding(), start = 15.dp, end = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // server address input
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp),
                    value = viewModel.serverAddress,
                    onValueChange = { viewModel.serverAddress = it },
                    label = { Text("Server Address") },
                    placeholder = { Text("my.business-filemanager.de") },
                    shape = RoundedCornerShape(percent = 20)
                )

                // username input
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp),
                    value = viewModel.username,
                    onValueChange = { viewModel.username = it },
                    label = { Text("Username") },
                    placeholder = { Text("Type username here") },
                    shape = RoundedCornerShape(percent = 20)
                )

                // password input
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp),
                    value = viewModel.password,
                    onValueChange = { newText ->
                        viewModel.password = newText
                    },
                    label = {
                        Text(text = "Password")
                    },
                    placeholder = { Text(text = "Type password here") },
                    shape = RoundedCornerShape(percent = 20),
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        if (passwordVisible) {
                            IconButton(onClick = { passwordVisible = !passwordVisible }){
                                Icon(
                                    imageVector = vectorResource(Res.drawable.ic_visibility_on),
                                    contentDescription = "hide password"
                                )
                            }
                        } else {
                            IconButton(onClick = { passwordVisible = !passwordVisible }){
                                Icon(
                                    imageVector = vectorResource(Res.drawable.ic_visibility_off),
                                    contentDescription = "show password"
                                )
                            }
                        }
                    }
                )

                // show authentication state
                when (authState) {
                    is RequestCondition.IdleCondition -> {
                        // do nothing
                    }
                    is RequestCondition.LoadingCondition -> {
                        Text("Loading...")
                    }
                    is RequestCondition.ErrorCondition -> {
                        Text("Error: ${authState.getErrorMessage()}")
                    }
                    is RequestCondition.SuccessCondition -> {
                        Text("Login successful")
                    }
                }


                // login button
                ElevatedButton(
                    modifier = Modifier
                        .width(200.dp)
                        .height(50.dp),
                    onClick = {
                        viewModel.authenticateUser()
                    },
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        disabledContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                ) {
                    Text("Login")
                }
            }
        }
    }
}