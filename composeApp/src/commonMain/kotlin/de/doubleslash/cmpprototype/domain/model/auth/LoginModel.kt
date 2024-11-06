package de.doubleslash.cmpprototype.domain.model.auth

/** model of data class dto LoginDTO
 * contains only necessary data for the login
 * contains only data for displaying in the UI
 * */
data class LoginModel (
    val sessionId: String,
    val userId: Int
)