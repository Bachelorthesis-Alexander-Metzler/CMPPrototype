package de.doubleslash.cmpprototype.domain.services.rest.auth.dto

import de.doubleslash.cmpprototype.domain.model.auth.LoginModel
import kotlinx.serialization.Serializable


/** data transfer object for login
 * contains response data from API
 * */
@Serializable
data class LoginDTO(
    val sessionId: String,
    val userId: Int
)

fun LoginDTO.toLoginModel(): LoginModel {
    return LoginModel(
        sessionId = sessionId,
        userId = userId
    )
}