package io.culturebook.auth.states

import androidx.annotation.StringRes

sealed interface RegistrationState {
    data class Error(@StringRes val message: Int) : RegistrationState
    object Success : RegistrationState
    object Loading : RegistrationState
    object Idle : RegistrationState
}