package de.doubleslash.cmpprototype.ui.presentation.screen.login.components

import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition
import org.jetbrains.compose.resources.stringResource


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import cmpprototype.composeapp.generated.resources.*
import de.doubleslash.cmpprototype.domain.model.auth.LoginModel
import de.doubleslash.cmpprototype.ui.presentation.screen.tabs.MainTabScreen
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ServerAddressField(serverAddress: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp),
        value = serverAddress,
        onValueChange = onValueChange,
        label = { Text(stringResource(Res.string.server_address_label)) },
        placeholder = { Text(stringResource(Res.string.server_address_placeholder)) },
        shape = RoundedCornerShape(percent = 20)
    )
}

@Composable
fun UsernameField(username: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp),
        value = username,
        onValueChange = onValueChange,
        label = { Text(stringResource(Res.string.username_label)) },
        placeholder = { Text(stringResource(Res.string.username_placeholder)) },
        shape = RoundedCornerShape(percent = 20)
    )
}

@Composable
fun PasswordField(password: String, onValueChange: (String) -> Unit, passwordVisible: Boolean, onPasswordVisibilityChange: () -> Unit) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp),
        value = password,
        onValueChange = onValueChange,
        label = { Text(stringResource(Res.string.password_label)) },
        placeholder = { Text(stringResource(Res.string.password_placeholder)) },
        shape = RoundedCornerShape(percent = 20),
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            IconButton(onClick = onPasswordVisibilityChange) {
                Icon(
                    imageVector = if (passwordVisible) vectorResource(Res.drawable.ic_visibility_on)
                    else vectorResource(Res.drawable.ic_visibility_off),
                    contentDescription = stringResource(Res.string.content_description_visibility_on)
                )
            }
        }
    )
}

@Composable
fun LoginButton(onClick: () -> Unit, enabled: Boolean) {
    ElevatedButton(
        modifier = Modifier
            .width(200.dp)
            .height(50.dp),
        onClick = onClick,
        enabled = enabled,
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
            disabledContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
    ) {
        Text(stringResource(Res.string.login_button_text))
    }
}

@Composable
fun AuthenticationState(authState: RequestCondition<LoginModel>, navigator: Navigator) {
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

            // on success navigate to file screen
            navigator.push(MainTabScreen())
        }
    }
}
