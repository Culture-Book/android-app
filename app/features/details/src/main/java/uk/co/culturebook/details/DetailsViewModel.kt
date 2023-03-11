package uk.co.culturebook.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.co.culturebook.data.remote.interfaces.ApiResponse
import uk.co.culturebook.data.repositories.cultural.NearbyRepository
import uk.co.culturebook.data.repositories.cultural.UpdateRepository
import uk.co.culturebook.ui.R
import java.util.*

class DetailsViewModel(
    private val nearbyRepository: NearbyRepository,
    private val updateRepository: UpdateRepository
) : ViewModel() {
    private val _detailStateFlow = MutableStateFlow<DetailState>(DetailState.Idle)
    val detailStateFlow = _detailStateFlow.asStateFlow()

    fun postEvent(event: DetailEvent) {
        viewModelScope.launch {
            _detailStateFlow.value = DetailState.Loading
            when (event) {
                DetailEvent.Idle -> _detailStateFlow.emit(DetailState.Idle)
                is DetailEvent.BlockContribution -> blockContribution(event.uuid)
                is DetailEvent.BlockElement -> blockElement(event.uuid)
                is DetailEvent.FavouriteContribution -> favouriteContribution(event.contribution.id)
                is DetailEvent.FavouriteElement -> favouriteElement(event.element.id)
                is DetailEvent.GetContribution -> getContribution(event.uuid)
                is DetailEvent.GetElement -> getElement(event.uuid)
            }
        }
    }

    private suspend fun getElement(uuid: UUID?) {
        when (val response = nearbyRepository.getElement(uuid)) {
            is ApiResponse.Success -> _detailStateFlow.emit(
                DetailState.ElementReceived(response.data)
            )
            else -> _detailStateFlow.emit(DetailState.Error(R.string.generic_sorry))
        }
    }

    private suspend fun getContribution(uuid: UUID?) {
        when (val response = nearbyRepository.getContribution(uuid)) {
            is ApiResponse.Success -> _detailStateFlow.emit(
                DetailState.ContributionReceived(response.data)
            )
            else -> _detailStateFlow.emit(DetailState.Error(R.string.generic_sorry))
        }
    }

    private suspend fun blockElement(uuid: UUID?) {
        when (updateRepository.blockElement(uuid)) {
            is ApiResponse.Success, is ApiResponse.Success.Empty ->
                _detailStateFlow.emit(DetailState.Blocked)
            else -> _detailStateFlow.emit(DetailState.Error(R.string.generic_sorry))
        }
    }

    private suspend fun blockContribution(uuid: UUID?) {
        when (updateRepository.blockContribution(uuid)) {
            is ApiResponse.Success, is ApiResponse.Success.Empty ->
                _detailStateFlow.emit(DetailState.Success)
            else -> _detailStateFlow.emit(DetailState.Error(R.string.generic_sorry))
        }
    }

    private suspend fun favouriteElement(uuid: UUID?) {
        when (updateRepository.favouriteElement(uuid)) {
            is ApiResponse.Success, is ApiResponse.Success.Empty ->
                postEvent(DetailEvent.GetElement(uuid))
            else -> _detailStateFlow.emit(DetailState.Error(R.string.generic_sorry))
        }
    }

    private suspend fun favouriteContribution(uuid: UUID?) {
        when (updateRepository.favouriteContribution(uuid)) {
            is ApiResponse.Success, is ApiResponse.Success.Empty ->
                postEvent(DetailEvent.GetContribution(uuid))
            else -> _detailStateFlow.emit(DetailState.Error(R.string.generic_sorry))
        }
    }

}