package io.culturebook.auth.states

import io.culturebook.nav.Route

sealed interface LoginState {
    data class Redirection(val destination: Route) : LoginState
    data class Error(val message: String) : LoginState
    object Loading : LoginState
    object Idle : LoginState
}