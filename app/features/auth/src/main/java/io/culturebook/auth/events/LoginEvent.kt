package io.culturebook.auth.events

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed interface LoginEvent {
    data class Login(val email: String, val password: String) : LoginEvent
    object Idle : LoginEvent
}

@Stable
class LoginHState {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
}