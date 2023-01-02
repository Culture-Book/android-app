package uk.co.culturebook.auth.states

import androidx.annotation.StringRes
import uk.co.culturebook.ui.R

sealed interface RegistrationState {
    data class Error(@StringRes val messageId: Int = R.string.generic_sorry) : RegistrationState
    object Success : RegistrationState
    object Loading : RegistrationState
    object Idle : RegistrationState
}