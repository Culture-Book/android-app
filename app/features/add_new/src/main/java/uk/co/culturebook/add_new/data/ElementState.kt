package uk.co.culturebook.add_new.data

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import uk.co.culturebook.data.models.cultural.*
import java.util.*

@Stable
class ElementState {
    var culture: Culture? by mutableStateOf(null)
    var name: String by mutableStateOf("")
    var type: ElementType? by mutableStateOf(null)
    var location: Location? by mutableStateOf(null)
    var information: String by mutableStateOf("")
    var eventType: EventType? by mutableStateOf(null)
    var linkElements: List<UUID> by mutableStateOf(emptyList())
    var files by mutableStateOf<List<MediaFile>>(emptyList())

    fun toElement() = Element(
        cultureId = culture?.id!!,
        name = name,
        type = type!!,
        location = location!!,
        information = information,
        eventType = eventType,
        linkElements = linkElements
    )
}