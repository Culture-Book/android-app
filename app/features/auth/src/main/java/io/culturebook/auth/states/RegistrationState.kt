package io.culturebook.auth.states

import androidx.annotation.StringRes
import io.culturebook.ui.R

sealed interface RegistrationState {
    data class Error(@StringRes val messageId: Int = R.string.generic_sorry) : RegistrationState
    object Success : RegistrationState
    object Loading : RegistrationState
    object Idle : RegistrationState
}