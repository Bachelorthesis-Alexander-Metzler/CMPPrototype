package de.doubleslash.cmpprototype.ui.presentation.screen.login.components

import org.jetbrains.compose.resources.stringResource


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cmpprototype.composeapp.generated.resources.*
import de.doubleslash.cmpprototype.ui.presentation.screen.login.LoginViewModel
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveButton
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveCheckbox
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ServerAddressField(modifier: Modifier, serverAddress: String, enabled: Boolean, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        modifier = modifier,
        value = serverAddress,
        enabled = enabled,
        onValueChange = onValueChange,
        label = { Text(stringResource(Res.string.server_address_label)) },
        placeholder = { Text(stringResource(Res.string.server_address_placeholder)) },
        shape = RoundedCornerShape(percent = 20)
    )
}

@Composable
fun UsernameField(modifier: Modifier,username: String, enabled: Boolean, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        modifier = modifier,
        value = username,
        enabled = enabled,
        onValueChange = onValueChange,
        label = { Text(stringResource(Res.string.username_label)) },
        placeholder = { Text(stringResource(Res.string.username_placeholder)) },
        shape = RoundedCornerShape(percent = 20)
    )
}

@Composable
fun PasswordField(modifier: Modifier, password: String, enabled: Boolean, onValueChange: (String) -> Unit, passwordVisible: Boolean, onPasswordVisibilityChange: () -> Unit) {
    OutlinedTextField(
        modifier = modifier,
        value = password,
        enabled = enabled,
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

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun PreviouslyAuthenticatedCheckbox(modifier: Modifier, viewModel: LoginViewModel) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AdaptiveCheckbox(
            checked = viewModel.isPreviouslyAuthenticated,
            onCheckedChange = null
        )
        Text(
            text = stringResource(Res.string.previously_authenticated),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun LoginButton(modifier: Modifier, onClick: () -> Unit, enabled: Boolean) {
    AdaptiveButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        adaptation = {
            material {
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    disabledContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
            }
        },
        content = {
            Text(stringResource(Res.string.login_button_text))
        }
    )

//    ElevatedButton(
//        modifier = modifier,
//        onClick = onClick,
//        enabled = enabled,
//        colors = ButtonColors(
//            containerColor = MaterialTheme.colorScheme.primaryContainer,
//            contentColor = MaterialTheme.colorScheme.primary,
//            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
//            disabledContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
//        )
//    ) {
//        Text(stringResource(Res.string.login_button_text))
//    }
}
