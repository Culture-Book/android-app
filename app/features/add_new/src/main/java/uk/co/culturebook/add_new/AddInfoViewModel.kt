package uk.co.culturebook.add_new

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.co.culturebook.add_new.data.InfoData
import uk.co.culturebook.add_new.info.events.AddInfoEvent
import uk.co.culturebook.add_new.info.states.AddInfoState
import uk.co.culturebook.data.models.cultural.*
import uk.co.culturebook.ui.R
import java.time.LocalDateTime
import java.util.*

class AddInfoViewModel : ViewModel() {
    private val _addInfoState = MutableStateFlow<AddInfoState>(AddInfoState.Idle)
    val infoData = InfoData()
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
            infoData.eventType = infoData.eventType.copy(startDateTime = startDateTime)
            _addInfoState.emit(AddInfoState.AddedDate(infoData = infoData))
        }
    }

    private fun registerEventLocation(location: Location) {
        viewModelScope.launch {
            infoData.eventType = infoData.eventType.copy(location = location)
            _addInfoState.emit(AddInfoState.AddedLocation(infoData))
        }
    }

    private fun registerLinkedElements(elements: List<UUID>) {
        viewModelScope.launch {
            infoData.linkedElements = elements
            _addInfoState.emit(AddInfoState.AddLinkElements(infoData))
        }
    }

    private fun addFiles(files: List<MediaFile>) {
        viewModelScope.launch {
            val newFiles = mutableListOf<MediaFile>()

            for (file in files) {
                val fileExists = infoData.files.contains(file)
                val isFileValid = file.isContentTypeValid() && file.fileSize > 0 && !fileExists
                if (!isFileValid) {
                    _addInfoState.emit(AddInfoState.Error(R.string.invalid_file))
                    return@launch
                } else {
                    newFiles.add(file)
                }
            }

            val newList = infoData.files + newFiles

            if (newList.smallerThan50Mb()) {
                infoData.files = newList
                _addInfoState.emit(AddInfoState.AddedFiles(infoData))
            } else {
                _addInfoState.emit(AddInfoState.Error(R.string.files_Size))
            }
        }
    }

    private fun deleteFile(mediaFile: MediaFile) {
        viewModelScope.launch {
            val newList = infoData.files - setOf(mediaFile)
            infoData.files = newList
            _addInfoState.emit(AddInfoState.DeleteMedia(infoData))
        }
    }

    private fun submit(background: String) {
        viewModelScope.launch {
            if (background.trim().isEmpty()) {
                _addInfoState.emit(AddInfoState.Error(R.string.background_required))
                return@launch
            }
            if (infoData.eventType.location.isEmpty()) {
                _addInfoState.emit(AddInfoState.Error(R.string.event_location_required))
                return@launch
            }
            if (infoData.eventType.startDateTime == LocalDateTime.MIN) {
                _addInfoState.emit(AddInfoState.Error(R.string.event_location_required))
                return@launch
            }
            infoData.background = background
            _addInfoState.emit(
                AddInfoState.NavigateNext(infoData)
            )
        }
    }
}