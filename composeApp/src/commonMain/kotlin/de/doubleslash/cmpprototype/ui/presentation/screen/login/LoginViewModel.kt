package de.doubleslash.cmpprototype.ui.presentation.screen.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import de.doubleslash.cmpprototype.domain.model.auth.LoginModel
import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition
import de.doubleslash.cmpprototype.domain.use_case.authenticateUser.AuthenticateUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authUserUseCase: AuthenticateUserUseCase // dependency injection
) : ScreenModel {
    var serverAddress by mutableStateOf("")
    var username by mutableStateOf("")
    var password by mutableStateOf("")

    // current state of authentication process
    var authState by mutableStateOf<RequestCondition<LoginModel>>(RequestCondition.IdleCondition)


    fun authenticateUser() {
        screenModelScope.launch(Dispatchers.Main) {
            // set state to loading
            authState = RequestCondition.LoadingCondition

            // try authentication
            try {
                val result = authUserUseCase.invoke(serverAddress, username, password)

                // if successful, set state to success
                authState = result
            } catch (e: Exception) {
                // if error, set state to error
                authState = RequestCondition.ErrorCondition("Unexpected error: ${e.message}")
            }
        }
    }
}