package io.culturebook.auth.states

sealed interface LoginState {
    data class Redirection(val destination: String) : LoginState
    data class Error(val message: String) : LoginState
    object Loading : LoginState
    object Idle : LoginState
}