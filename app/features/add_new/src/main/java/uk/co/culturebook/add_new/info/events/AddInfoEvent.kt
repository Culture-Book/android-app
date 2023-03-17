package uk.co.culturebook.add_new.info.events

import androidx.annotation.StringRes
import uk.co.culturebook.data.models.cultural.Element
import uk.co.culturebook.data.models.cultural.Location
import uk.co.culturebook.data.models.cultural.MediaFile
import java.time.LocalDateTime

sealed interface AddInfoEvent {
    object Idle : AddInfoEvent
    object Loading : AddInfoEvent
    data class Error(@StringRes val errorId: Int) : AddInfoEvent
    data class Submit(val background: String) : AddInfoEvent
    data class LinkElements(val elements: List<Element> = emptyList()) : AddInfoEvent
    data class AddFile(val files: List<MediaFile>) : AddInfoEvent
    data class AddDate(val date: LocalDateTime) : AddInfoEvent
    data class AddLocation(val location: Location) : AddInfoEvent
    data class DeleteMediaFile(val mediaFile: MediaFile) : AddInfoEvent
}