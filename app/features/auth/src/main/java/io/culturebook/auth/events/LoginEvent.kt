package io.culturebook.auth.events

sealed interface LoginEvent {
    data class Login(val email: String, val password: String) : LoginEvent
    object Idle : LoginEvent
}