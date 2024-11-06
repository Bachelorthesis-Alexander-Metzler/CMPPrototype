package de.doubleslash.cmpprototype.data.datasource.remote.rest

import de.doubleslash.cmpprototype.data.datasource.remote.rest.dto.LoginDTO
import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition

interface AuthApi {
    suspend fun authenticateUser(
        serverAddress: String,
        username: String,
        password: String): RequestCondition<LoginDTO>
}