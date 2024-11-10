package de.doubleslash.cmpprototype.ui.presentation.screen.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.plusmobileapps.konnectivity.NetworkConnection
import de.doubleslash.cmpprototype.domain.model.auth.LoginModel
import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition
import de.doubleslash.cmpprototype.domain.use_case.authenticateUser.AuthenticateUserUseCase
import de.doubleslash.cmpprototype.domain.use_case.checkNetworkStatus.GetConnectionStatusUseCase
import de.doubleslash.cmpprototype.domain.use_case.checkNetworkStatus.GetNetworkStatusUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authUserUseCase: AuthenticateUserUseCase, // dependency injection
    private val getConnectionStatusUseCase: GetConnectionStatusUseCase,
    private val getNetworkConnectionUseCase: GetNetworkStatusUseCase
) : ScreenModel {
    // input fields
    var serverAddress by mutableStateOf("")
    var username by mutableStateOf("")
    var password by mutableStateOf("")

    // current state of authentication process
    var authState by mutableStateOf<RequestCondition<LoginModel>>(RequestCondition.IdleCondition)

    // network status
    var networkStatus by mutableStateOf(NetworkConnection.NONE)
    var isConnected by mutableStateOf(false)

    init {
        // Collect the connection status changes from CheckConnectionUseCase
        screenModelScope.launch(Dispatchers.IO) {
            getConnectionStatusUseCase.invoke().collectLatest { status ->
                isConnected = status
            }
        }
        // Collect the network status changes from GetNetworkStatusUseCase
        screenModelScope.launch(Dispatchers.IO) {
            getNetworkConnectionUseCase.invoke().collectLatest { status ->
                networkStatus = status
            }
        }
    }


    fun onLoginClick() {
        screenModelScope.launch(Dispatchers.Main) {
            if (!isConnected) {
                // TODO: isPreviouslyAuthenticated
                println("Not connected to the internet")
            } else {
                println("Connected to the internet")
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
}