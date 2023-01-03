package uk.co.culturebook.auth.states

import androidx.annotation.StringRes
import uk.co.culturebook.ui.R

sealed interface ForgotState {
    data class Error(@StringRes val messageId: Int = R.string.generic_sorry) :
        ForgotState

    object Success : ForgotState
    object Loading : ForgotState
    object Idle : ForgotState
}