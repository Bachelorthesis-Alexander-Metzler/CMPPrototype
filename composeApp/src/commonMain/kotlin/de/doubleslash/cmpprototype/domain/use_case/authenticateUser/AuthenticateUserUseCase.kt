package de.doubleslash.cmpprototype.domain.use_case.authenticateUser

import de.doubleslash.cmpprototype.domain.model.auth.LoginModel
import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition
import de.doubleslash.cmpprototype.domain.repository.secureStore.SecureStoreRepository
import de.doubleslash.cmpprototype.domain.services.rest.auth.AuthService
import de.doubleslash.cmpprototype.domain.services.rest.auth.dto.toLoginModel

/** business logic for authentication of a user
 * */
class AuthenticateUserUseCase(
    private val repository: SecureStoreRepository, // dependency injection of interface to avoid hard coding an actual implementation
    // this way this class can be tested with a mock implementation
    private val api: AuthService // dependency injection of interface to avoid hard coding an actual implementation
) {

    suspend operator fun invoke(
        serverAddress: String,
        username: String,
        password: String
    ): RequestCondition<LoginModel> {
        return authenticateUser(serverAddress, username, password)
    }

    private suspend fun authenticateUser(
        serverAddress: String,
        username: String,
        password: String
    ): RequestCondition<LoginModel> {
        // call the API to authenticate the user
        val requestCondition = api.authenticateUser(serverAddress, username, password)

        return when (requestCondition) {
            is RequestCondition.SuccessCondition -> {
                // store session data and credentials if authentication was successful
                repository.saveServerAddress(serverAddress)
                repository.saveUsername(username)
                repository.savePassword(password)
                repository.saveSessionId(requestCondition.data.sessionId)
                repository.saveUserId(requestCondition.data.userId)
                repository.savePreviouslyAuthenticated()

                // map data to login model for UI
                val loginModel = requestCondition.data.toLoginModel()
                // return success object
                RequestCondition.SuccessCondition(data = loginModel)
            }

            is RequestCondition.ErrorCondition -> requestCondition // return error object as is
            else -> RequestCondition.ErrorCondition("Unexpected error")
        }
    }
}