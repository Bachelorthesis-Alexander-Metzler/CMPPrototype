package de.doubleslash.cmpprototype.domain.model.auth

data class CredentialsModel(
    val serverAddress: String,
    val username: String,
    val password: String
)
