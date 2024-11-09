package de.doubleslash.cmpprototype.data.repository

import de.doubleslash.cmpprototype.data.datasource.remote.rest.auth.AuthApi
import de.doubleslash.cmpprototype.data.datasource.remote.rest.auth.dto.LoginDTO
import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition
import de.doubleslash.cmpprototype.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: AuthApi // dependency injection
) : AuthRepository {

    override suspend fun authenticateUser(
        serverAddress: String,
        username: String,
        password: String
    ): RequestCondition<LoginDTO> {
        return api.authenticateUser(serverAddress, username, password)
    }
}