package de.doubleslash.cmpprototype.domain.services.rest.auth

import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition
import de.doubleslash.cmpprototype.domain.services.rest.auth.dto.LoginDTO

interface AuthService {
    suspend fun authenticateUser(
        serverAddress: String,
        username: String,
        password: String
    ): RequestCondition<LoginDTO>
}