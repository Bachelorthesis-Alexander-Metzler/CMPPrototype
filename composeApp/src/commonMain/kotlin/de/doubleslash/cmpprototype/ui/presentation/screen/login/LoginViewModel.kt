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
import de.doubleslash.cmpprototype.domain.use_case.getSessionData.GetSessionDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    // dependency injection
    private val authUserUseCase: AuthenticateUserUseCase,
    private val getConnectionStatusUseCase: GetConnectionStatusUseCase,
    private val getNetworkConnectionUseCase: GetNetworkStatusUseCase,
    private val getSessionDataUseCase: GetSessionDataUseCase
) : ScreenModel {
    // input fields
    var serverAddress by mutableStateOf("")
    var username by mutableStateOf("")
    var password by mutableStateOf("")

    // placeholder checkbox value for isPreviouslyAuthenticated (no requirement)
    var isPreviouslyAuthenticated by mutableStateOf(false)

    // current state of authentication process
    var authState by mutableStateOf<RequestCondition<LoginModel>>(RequestCondition.IdleCondition)

    // network status
    var networkStatus: StateFlow<NetworkConnection> = getNetworkConnectionUseCase.invoke()
    var isConnected: StateFlow<Boolean> = getConnectionStatusUseCase.invoke()

    // placeholder checkbox value for local auth active (implementation not possible)
    var isLocalAuthActive by mutableStateOf(false)


    fun onLoginClick() {
        screenModelScope.launch(Dispatchers.Main) {
            if (!isConnected.value) {
                println("Not connected to the internet")
                authState = RequestCondition.LoadingCondition

                if (!isPreviouslyAuthenticated) {
                    // user cannot login
                    authState = RequestCondition.ErrorCondition("Internet connection required")
                } else {
                    if (!isLocalAuthActive) {
                        // TODO: get sessionId und userId from local storage
                        val loginModel: LoginModel = getSessionData()
                        authState = RequestCondition.SuccessCondition(loginModel)
                    } else {
                        // login via local auth
                        loginViaLocalAuthOffline()
                    }
                }
            } else {
                println("Connected to the internet")

                if (!isPreviouslyAuthenticated || !isLocalAuthActive) {
                    loginViaREST()
                } else {
                    // local auth is active or user is previously authenticated
                    loginViaLocalAuthOnline()
                }
            }
        }
    }

    /** Login via local authentication with REST authentication */
    private suspend fun loginViaLocalAuthOnline() {
        val isLocalAuthSuccessful = true

        if (isLocalAuthSuccessful) {
            loginViaREST()
        } else {
            authState = RequestCondition.ErrorCondition("Local authentication failed")
        }
    }

    /** Login via local authentication without REST authentication */
    private fun loginViaLocalAuthOffline() {
        // TODO: get local auth status
        val isLocalAuthSuccessful = true

        if (isLocalAuthSuccessful) {
            // TODO: get sessionId und userId from local storage
            val loginModel: LoginModel = getSessionData()
            authState = RequestCondition.SuccessCondition(loginModel)
        } else {
            authState = RequestCondition.ErrorCondition("Local authentication failed")
        }
    }

    private fun getSessionData(): LoginModel {
        // get sessionId und userId from local storage
        return getSessionDataUseCase.invoke()
    }

    private suspend fun loginViaREST() {
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