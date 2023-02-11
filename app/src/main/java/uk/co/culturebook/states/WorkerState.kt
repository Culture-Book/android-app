package uk.co.culturebook.states

import androidx.annotation.StringRes


sealed interface WorkerState {
    data class ShowUploadMessage(@StringRes val stringId: Int) : WorkerState
    data class ShowUploadComplete(@StringRes val stringId: Int) : WorkerState
    data class ShowUploadFailed(@StringRes val stringId: Int) : WorkerState
    object Idle : WorkerState
}