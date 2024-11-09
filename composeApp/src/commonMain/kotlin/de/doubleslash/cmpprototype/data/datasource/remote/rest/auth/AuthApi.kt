package de.doubleslash.cmpprototype.data.datasource.remote.rest.auth

import de.doubleslash.cmpprototype.data.datasource.remote.rest.auth.dto.LoginDTO
import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition

interface AuthApi {
    suspend fun authenticateUser(
        serverAddress: String,
        username: String,
        password: String): RequestCondition<LoginDTO>
}