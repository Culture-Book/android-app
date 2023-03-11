package uk.co.culturebook.details

import androidx.annotation.StringRes

sealed interface DetailState {
    object Idle : DetailState
    object Success : DetailState
    object Loading : DetailState
    data class Error(@StringRes val error: Int) : DetailState
}