package de.doubleslash.cmpprototype.common

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object SessionManager {
    private val _isUserLoggedIn = MutableStateFlow(false) // Default: Not logged in
    val isUserLoggedIn: StateFlow<Boolean> get() = _isUserLoggedIn

    fun login() {
        _isUserLoggedIn.value = true
    }

    fun logout() {
        _isUserLoggedIn.value = false
    }
}
