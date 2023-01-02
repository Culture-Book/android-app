package uk.co.culturebook.auth.states

import androidx.annotation.StringRes

sealed interface LoginState {
    data class Error(@StringRes val message: Int) : LoginState
    object Success : LoginState
    object Loading : LoginState
    object Idle : LoginState
}