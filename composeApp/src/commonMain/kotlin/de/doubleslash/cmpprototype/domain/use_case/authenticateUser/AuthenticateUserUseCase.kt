package de.doubleslash.cmpprototype.domain.use_case.authenticateUser

import de.doubleslash.cmpprototype.data.datasource.remote.rest.auth.dto.toLoginModel
import de.doubleslash.cmpprototype.domain.model.auth.LoginModel
import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition
import de.doubleslash.cmpprototype.domain.repository.auth.AuthRepository

/** business logic for authentication of a user
 * */
class AuthenticateUserUseCase (
    private val repository: AuthRepository // dependency injection of interface to avoid hard coding an actual implementation
                                            // this way this class can be tested with a mock implementation
) {

    suspend operator fun invoke(
        serverAddress: String,
        username: String,
        password: String
    ): RequestCondition<LoginModel> {
        val loginDTO = repository.authenticateUser(serverAddress, username, password)

        // convert result to login model or return error
        return when (loginDTO) {
            is RequestCondition.SuccessCondition -> {
                RequestCondition.SuccessCondition(data = loginDTO.data.toLoginModel())
            }
            is RequestCondition.ErrorCondition -> loginDTO // return error object as is
            else -> RequestCondition.ErrorCondition("Unexpected error")
        }
    }
}