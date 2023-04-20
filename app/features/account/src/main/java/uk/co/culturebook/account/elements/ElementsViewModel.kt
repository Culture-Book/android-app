package uk.co.culturebook.account.elements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.co.culturebook.data.models.cultural.SearchCriteriaState
import uk.co.culturebook.data.remote.interfaces.ApiResponse
import uk.co.culturebook.data.remote.interfaces.getDataOrNull
import uk.co.culturebook.data.repositories.cultural.ElementsRepository
import java.util.UUID

class ElementsViewModel(
    private val elementsRepository: ElementsRepository
) : ViewModel() {
    val searchCriteriaState = SearchCriteriaState()
    private val _state = MutableStateFlow<ElementsState>(ElementsState.Idle)
    val state get() = _state.asStateFlow()

    fun postEvent(event: ElementsEvent) {
        viewModelScope.launch {
            when (event) {
                is ElementsEvent.FetchElements -> if (searchCriteriaState.getFavourites) fetchFavouriteElements() else fetchElements()
                is ElementsEvent.FetchContributions -> if (searchCriteriaState.getFavourites) fetchFavouriteContributions() else fetchContributions()
                is ElementsEvent.FetchCultures -> if (searchCriteriaState.getFavourites) fetchFavouriteCultures() else fetchCultures()
                is ElementsEvent.Idle -> updateState(ElementsState.Idle)
                is ElementsEvent.ConfirmDeleteContribution -> deleteContribution(event.uuid)
                is ElementsEvent.ConfirmDeleteCulture -> deleteCulture(event.uuid)
                is ElementsEvent.ConfirmDeleteElement -> deleteElement(event.uuid)
                is ElementsEvent.DeleteContribution ->
                    updateState(ElementsState.DeleteContribution(event.uuid))

                is ElementsEvent.DeleteCulture -> updateState(ElementsState.DeleteCulture(event.uuid))
                is ElementsEvent.DeleteElement -> updateState(ElementsState.DeleteElement(event.uuid))
            }
        }
    }

    private suspend fun fetchElements() {
        when (val response =
            elementsRepository.getUserElements(searchCriteriaState.toSearchCriteria())) {
            is ApiResponse.Success.Empty, is ApiResponse.Success -> {
                updateState(ElementsState.ElementsFetched(response.getDataOrNull() ?: emptyList()))
            }

            is ApiResponse.Exception, is ApiResponse.Failure -> {
                updateState(ElementsState.Error())
            }
        }
    }

    private suspend fun fetchContributions() {
        when (val response =
            elementsRepository.getUserContributions(searchCriteriaState.toSearchCriteria())) {
            is ApiResponse.Success.Empty, is ApiResponse.Success -> {
                updateState(
                    ElementsState.ContributionsFetched(
                        response.getDataOrNull() ?: emptyList()
                    )
                )
            }

            is ApiResponse.Exception, is ApiResponse.Failure -> {
                updateState(ElementsState.Error())
            }
        }
    }

    private suspend fun fetchCultures() {
        when (val response =
            elementsRepository.getUserCultures(searchCriteriaState.toSearchCriteria())) {
            is ApiResponse.Success.Empty, is ApiResponse.Success -> {
                updateState(ElementsState.CulturesFetched(response.getDataOrNull() ?: emptyList()))
            }

            is ApiResponse.Exception, is ApiResponse.Failure -> {
                updateState(ElementsState.Error())
            }
        }
    }

    private suspend fun fetchFavouriteElements() {
        when (val response =
            elementsRepository.getFavouriteElements(searchCriteriaState.toSearchCriteria())) {
            is ApiResponse.Success.Empty, is ApiResponse.Success -> {
                updateState(
                    ElementsState.FavouriteElementsFetched(
                        response.getDataOrNull() ?: emptyList()
                    )
                )
            }

            is ApiResponse.Exception, is ApiResponse.Failure -> {
                updateState(ElementsState.Error())
            }
        }
    }

    private suspend fun fetchFavouriteContributions() {
        when (val response =
            elementsRepository.getFavouriteContributions(searchCriteriaState.toSearchCriteria())) {
            is ApiResponse.Success.Empty, is ApiResponse.Success -> {
                updateState(
                    ElementsState.FavouriteContributionsFetched(
                        response.getDataOrNull() ?: emptyList()
                    )
                )
            }

            is ApiResponse.Exception, is ApiResponse.Failure -> {
                updateState(ElementsState.Error())
            }
        }
    }

    private suspend fun fetchFavouriteCultures() {
        when (val response =
            elementsRepository.getFavouriteCultures(searchCriteriaState.toSearchCriteria())) {
            is ApiResponse.Success.Empty, is ApiResponse.Success -> {
                updateState(
                    ElementsState.FavouriteCulturesFetched(
                        response.getDataOrNull() ?: emptyList()
                    )
                )
            }

            is ApiResponse.Exception, is ApiResponse.Failure -> {
                updateState(ElementsState.Error())
            }
        }
    }

    private suspend fun deleteElement(uuid: UUID) {
        when (elementsRepository.deleteElement(uuid)) {
            is ApiResponse.Success.Empty, is ApiResponse.Success -> {
                postEvent(ElementsEvent.FetchElements)
            }

            is ApiResponse.Exception, is ApiResponse.Failure -> {
                updateState(ElementsState.Error())
            }
        }
    }

    private suspend fun deleteContribution(uuid: UUID) {
        when (elementsRepository.deleteContribution(uuid)) {
            is ApiResponse.Success.Empty, is ApiResponse.Success -> {
                postEvent(ElementsEvent.FetchContributions)
            }

            is ApiResponse.Exception, is ApiResponse.Failure -> {
                updateState(ElementsState.Error())
            }
        }
    }

    private suspend fun deleteCulture(uuid: UUID) {
        when (elementsRepository.deleteCulture(uuid)) {
            is ApiResponse.Success.Empty, is ApiResponse.Success -> {
                postEvent(ElementsEvent.FetchCultures)
            }

            is ApiResponse.Exception, is ApiResponse.Failure -> {
                updateState(ElementsState.Error())
            }
        }
    }

    private fun updateState(state: ElementsState) {
        _state.value = state
    }
}