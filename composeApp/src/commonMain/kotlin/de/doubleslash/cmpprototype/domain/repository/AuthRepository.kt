package de.doubleslash.cmpprototype.domain.repository

import de.doubleslash.cmpprototype.data.datasource.remote.rest.auth.dto.LoginDTO
import de.doubleslash.cmpprototype.domain.model.auth.RequestCondition

/**
 * Repository for managing user authentication, combining AuthService and AuthLocalDataSource.
 * Handles session creation, renewal, and local storage of credentials.
 */
interface AuthRepository {
    suspend fun authenticateUser(
        serverAddress: String,
        username: String,
        password: String): RequestCondition<LoginDTO>
}