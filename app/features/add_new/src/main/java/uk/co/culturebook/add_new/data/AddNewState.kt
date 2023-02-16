package uk.co.culturebook.add_new.data

import androidx.compose.runtime.*
import uk.co.culturebook.data.models.cultural.*
import java.util.*

@Stable
class AddNewState {
    var culture: Culture? by mutableStateOf(null)
    var parentElement: UUID? by mutableStateOf(null)
    val isContribution: Boolean by derivedStateOf { parentElement != null }
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

    fun toContribution() = Contribution(
        elementId = parentElement!!,
        name = name,
        type = type!!,
        location = location!!,
        information = information,
        eventType = eventType,
        linkElements = linkElements,
    )

    fun clear() {
        culture = null
        parentElement = null
        name = ""
        type = null
        location = null
        information = ""
        eventType = null
        linkElements = emptyList()
        files = emptyList()
    }
}