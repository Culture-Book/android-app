package uk.co.culturebook.add_new.link_elements

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.co.culturebook.data.models.cultural.Element
import uk.co.culturebook.data.models.cultural.SearchCriteria
import uk.co.culturebook.data.models.cultural.SearchCriteriaState
import uk.co.culturebook.data.remote.interfaces.ApiResponse
import uk.co.culturebook.data.repositories.cultural.ElementsRepository
import uk.co.culturebook.data.repositories.cultural.UpdateRepository
import java.util.*

class LinkElementsViewModel(
    initialSelectedElements: List<Element> = emptyList(),
    private val elementsRepository: ElementsRepository,
    private val updateRepository: UpdateRepository
) : ViewModel() {
    private val _linkState = MutableStateFlow<LinkState>(LinkState.Idle)
    val searchCriteria = SearchCriteriaState()
    val linkState = _linkState.asStateFlow()
    var selectedElements by mutableStateOf(initialSelectedElements)

    fun postEvent(event: LinkEvent) {
        viewModelScope.launch {
            when (event) {
                is LinkEvent.LinkElements ->
                    _linkState.value = LinkState.ElementsLinked(selectedElements)
                is LinkEvent.FetchElements -> fetchElements(event.searchCriteria.toSearchCriteria())
                is LinkEvent.BlockElement -> blockElement(event.uuid)
                is LinkEvent.FavouriteElement -> favouriteElement(event.uuid)
                is LinkEvent.LinkElement -> toggleSelection(event.element)
            }
        }
    }

    private fun toggleSelection(element: Element) {
        selectedElements = if (selectedElements.contains(element)) {
            selectedElements - element
        } else {
            selectedElements + element
        }
    }

    private suspend fun fetchElements(searchCriteria: SearchCriteria) {
        _linkState.emit(LinkState.Loading)

        val state = when (val response = elementsRepository.getElements(searchCriteria)) {
            is ApiResponse.Success.Empty,
            is ApiResponse.Exception,
            is ApiResponse.Failure -> LinkState.Error
            is ApiResponse.Success -> LinkState.ElementsFetched(response.data)
        }
        _linkState.emit(state)
    }

    private suspend fun favouriteElement(uuid: UUID?) {
        _linkState.emit(LinkState.Loading)

        when (updateRepository.favouriteElement(uuid)) {
            is ApiResponse.Success, is ApiResponse.Success.Empty ->
                postEvent(LinkEvent.FetchElements(searchCriteria))
            else -> _linkState.emit(LinkState.Error)
        }
    }

    private suspend fun blockElement(uuid: UUID?) {
        _linkState.emit(LinkState.Loading)

        when (updateRepository.blockElement(uuid)) {
            is ApiResponse.Success, is ApiResponse.Success.Empty -> postEvent(
                LinkEvent.FetchElements(
                    searchCriteria
                )
            )
            else -> _linkState.emit(LinkState.Error)
        }
    }

}