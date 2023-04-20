package uk.co.culturebook.add_new.data

import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import uk.co.culturebook.data.models.cultural.Contribution
import uk.co.culturebook.data.models.cultural.Culture
import uk.co.culturebook.data.models.cultural.Element
import uk.co.culturebook.data.models.cultural.ElementType
import uk.co.culturebook.data.models.cultural.EventType
import uk.co.culturebook.data.models.cultural.Location
import uk.co.culturebook.data.models.cultural.MediaFile
import java.util.UUID

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
    var linkElements: List<Element> by mutableStateOf(emptyList())
    var files by mutableStateOf<List<MediaFile>>(emptyList())

    fun toElement() = Element(
        cultureId = culture?.id!!,
        name = name,
        type = type!!,
        location = location!!,
        information = information,
        eventType = eventType,
        linkElements = linkElements.mapNotNull { it.id }
    )

    fun toContribution() = Contribution(
        elementId = parentElement!!,
        name = name,
        type = type!!,
        location = location!!,
        information = information,
        eventType = eventType,
        linkElements = linkElements.mapNotNull { it.id },
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