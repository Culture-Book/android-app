package uk.co.culturebook.add_new.info.states

import androidx.annotation.StringRes

sealed interface AddInfoState {
    object Idle : AddInfoState
    object Loading : AddInfoState
    object NavigateNext : AddInfoState
    object AddLinkElements : AddInfoState
    object AddedDate : AddInfoState
    object AddedLocation : AddInfoState
    object AddedFiles : AddInfoState
    data class Error(@StringRes val stringId: Int) : AddInfoState
    object DeleteMedia : AddInfoState
}
