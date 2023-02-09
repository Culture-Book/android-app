package uk.co.culturebook.add_new

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import uk.co.culturebook.add_new.data.ElementState
import uk.co.culturebook.add_new.data.InfoData
import uk.co.culturebook.data.models.cultural.*
import java.util.*

class AddNewViewModel : ViewModel() {
    private var _culture = MutableStateFlow<Culture?>(null)
    private var _name = MutableStateFlow("")
    private var _type = MutableStateFlow<ElementType?>(null)
    private var _location = MutableStateFlow<Location?>(null)
    private var _information = MutableStateFlow("")
    private var _eventType = MutableStateFlow<EventType?>(null)
    private var _linkElements = MutableStateFlow<List<UUID>>(emptyList())
    private var _files = MutableStateFlow<List<MediaFile>>(emptyList())

    val name = _name.asStateFlow()
    val type = _type.asStateFlow()
    val location = _location.asStateFlow()
    val information = _information.asStateFlow()
    val files = _files.asStateFlow()

    fun getElementState() = ElementState().apply {
        culture = _culture.value
        name = _name.value
        type = _type.value
        location = _location.value
        information = _information.value
        eventType = _eventType.value
        linkElements = _linkElements.value
        files = _files.value
    }

    fun registerLocation(culture: Culture, location: Location) {
        _culture.value = culture
        _location.value = location
    }

    fun registerTitleAndType(title: String, type: ElementType) {
        _name.value = title
        _type.value = type
    }

    fun registerInfo(infoData: InfoData) {
        _information.value = infoData.background
        _linkElements.value = infoData.linkedElements
        _files.value = infoData.files
        _eventType.value = infoData.eventType
    }
}