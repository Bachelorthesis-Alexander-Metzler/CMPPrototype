package de.doubleslash.cmpprototype.domain.use_case.authenticateUser

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
        return repository.authenticateUser(serverAddress, username, password)
    }
}