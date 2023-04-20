package uk.co.culturebook.add_new

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.co.culturebook.add_new.data.AddNewState
import uk.co.culturebook.add_new.info.events.AddInfoEvent
import uk.co.culturebook.add_new.info.states.AddInfoState
import uk.co.culturebook.data.models.cultural.Element
import uk.co.culturebook.data.models.cultural.ElementType
import uk.co.culturebook.data.models.cultural.Empty
import uk.co.culturebook.data.models.cultural.EventType
import uk.co.culturebook.data.models.cultural.Location
import uk.co.culturebook.data.models.cultural.MediaFile
import uk.co.culturebook.data.models.cultural.isContentTypeValid
import uk.co.culturebook.data.models.cultural.isEmpty
import uk.co.culturebook.data.models.cultural.smallerThan50Mb
import uk.co.culturebook.ui.R
import java.time.LocalDateTime

class AddInfoViewModel(
    private val addNewState: AddNewState
) : ViewModel() {
    private val _addInfoState = MutableStateFlow<AddInfoState>(AddInfoState.Idle)
    val addInfoState = _addInfoState.asStateFlow()

    fun postEvent(event: AddInfoEvent) {
        viewModelScope.launch {
            when (event) {
                is AddInfoEvent.Error -> _addInfoState.emit(AddInfoState.Error(event.errorId))
                is AddInfoEvent.AddDate -> registerEventDate(event.date)
                is AddInfoEvent.AddFile -> addFiles(event.files)
                is AddInfoEvent.AddLocation -> registerEventLocation(event.location)
                is AddInfoEvent.DeleteMediaFile -> deleteFile(event.mediaFile)
                is AddInfoEvent.LinkElements -> registerLinkedElements(event.elements)
                is AddInfoEvent.Submit -> submit(event.background)
                AddInfoEvent.Idle -> _addInfoState.emit(AddInfoState.Idle)
                AddInfoEvent.Loading -> _addInfoState.emit(AddInfoState.Loading)
            }
        }
    }

    private fun registerEventDate(startDateTime: LocalDateTime) {
        viewModelScope.launch {
            addNewState.eventType = addNewState.eventType?.copy(startDateTime = startDateTime)
                ?: EventType(startDateTime, Location.Empty)
            _addInfoState.emit(AddInfoState.AddedDate)
        }
    }

    private fun registerEventLocation(location: Location) {
        viewModelScope.launch {
            addNewState.eventType = addNewState.eventType?.copy(location = location)
                ?: EventType(LocalDateTime.MIN, location)
            _addInfoState.emit(AddInfoState.AddedLocation)
        }
    }

    private fun registerLinkedElements(elements: List<Element>) {
        viewModelScope.launch {
            addNewState.linkElements = elements
            _addInfoState.emit(AddInfoState.AddLinkElements)
        }
    }

    private fun addFiles(files: List<MediaFile>) {
        viewModelScope.launch {
            val newFiles = mutableListOf<MediaFile>()

            for (file in files) {
                val fileExists = addNewState.files.contains(file)
                val isFileValid = file.isContentTypeValid() && file.fileSize > 0 && !fileExists
                if (!isFileValid) {
                    _addInfoState.emit(AddInfoState.Error(R.string.invalid_file))
                    return@launch
                } else {
                    newFiles.add(file)
                }
            }

            val newList = addNewState.files + newFiles

            if (newList.smallerThan50Mb()) {
                addNewState.files = newList
                _addInfoState.emit(AddInfoState.AddedFiles)
            } else {
                _addInfoState.emit(AddInfoState.Error(R.string.files_Size))
            }
        }
    }

    private fun deleteFile(mediaFile: MediaFile) {
        viewModelScope.launch {
            val newList = addNewState.files - setOf(mediaFile)
            addNewState.files = newList
            _addInfoState.emit(AddInfoState.DeleteMedia)
        }
    }

    private fun submit(background: String) {
        viewModelScope.launch {
            if (background.trim().isEmpty()) {
                _addInfoState.emit(AddInfoState.Error(R.string.background_required))
                return@launch
            }
            if (addNewState.eventType?.location?.isEmpty() == true && addNewState.type == ElementType.Event) {
                _addInfoState.emit(AddInfoState.Error(R.string.event_location_required))
                return@launch
            }
            if (addNewState.eventType?.startDateTime == LocalDateTime.MIN && addNewState.type == ElementType.Event) {
                _addInfoState.emit(AddInfoState.Error(R.string.event_date_required))
                return@launch
            }
            addNewState.information = background
            _addInfoState.emit(
                AddInfoState.NavigateNext
            )
        }
    }
}