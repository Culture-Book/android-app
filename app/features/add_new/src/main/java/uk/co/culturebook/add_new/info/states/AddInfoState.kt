package uk.co.culturebook.add_new.info.states

import androidx.annotation.StringRes
import uk.co.culturebook.add_new.data.InfoData

sealed interface AddInfoState {
    object Idle : AddInfoState
    object Loading : AddInfoState
    data class NavigateNext(val infoData: InfoData) : AddInfoState
    data class AddLinkElements(val infoData: InfoData) : AddInfoState
    data class AddedDate(val infoData: InfoData) : AddInfoState
    data class AddedLocation(val infoData: InfoData) : AddInfoState
    data class AddedFiles(val infoData: InfoData) : AddInfoState
    data class Error(@StringRes val stringId: Int) : AddInfoState
    data class DeleteMedia(val infoData: InfoData) : AddInfoState
}
