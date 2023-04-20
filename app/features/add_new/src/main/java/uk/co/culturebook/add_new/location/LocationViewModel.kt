package uk.co.culturebook.add_new.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.co.culturebook.add_new.location.events.LocationEvent
import uk.co.culturebook.add_new.location.states.LocationState
import uk.co.culturebook.data.models.cultural.Culture
import uk.co.culturebook.data.models.cultural.CultureRequest
import uk.co.culturebook.data.models.cultural.Location
import uk.co.culturebook.data.remote.interfaces.ApiResponse
import uk.co.culturebook.data.repositories.cultural.AddNewRepository
import uk.co.culturebook.ui.R
import java.util.UUID

class LocationViewModel(private val addNewRepository: AddNewRepository) : ViewModel() {
    private val _locationState = MutableStateFlow<LocationState>(LocationState.ShowMap)
    val locationState = _locationState.asStateFlow()

    fun postEvent(locationEvent: LocationEvent) {
        viewModelScope.launch {
            _locationState.emit(LocationState.Loading)
            when (locationEvent) {
                is LocationEvent.ShowMap -> _locationState.emit(LocationState.ShowMap)
                is LocationEvent.AddCulture -> {
                    _locationState.emit(
                        addCulture(
                            locationEvent.cultureName,
                            locationEvent.location
                        )
                    )
                }

                is LocationEvent.SelectCulture -> {
                    _locationState.emit(
                        LocationState.SelectedCulture(
                            locationEvent.culture,
                            locationEvent.location
                        )
                    )
                }

                is LocationEvent.GetCultures -> _locationState.emit(getCultures(locationEvent.location))
                is LocationEvent.AddCultureRequest -> _locationState.emit(
                    LocationState.AddCulture(
                        locationEvent.location
                    )
                )
            }
        }
    }

    private suspend fun addCulture(cultureName: String, location: Location): LocationState {
        val culture = Culture(id = UUID.randomUUID(), name = cultureName, location = location)
        return when (val response =
            addNewRepository.addCulture(CultureRequest(culture, location))) {
            is ApiResponse.Success.Empty -> LocationState.ShowMap
            is ApiResponse.Exception -> LocationState.Error(response.errorMessage)
            is ApiResponse.Failure -> LocationState.Error(response.errorMessage)
            is ApiResponse.Success -> LocationState.SelectedCulture(response.data, location)
        }
    }

    private suspend fun getCultures(location: Location): LocationState {
        return when (val response = addNewRepository.getCultures(location = location)) {
            is ApiResponse.Success.Empty -> LocationState.ShowMap
            is ApiResponse.Exception -> LocationState.Error(response.errorMessage)
            is ApiResponse.Failure -> LocationState.Error(response.errorMessage)
            is ApiResponse.Success -> {
                val cultures = response.data.cultures
                if (cultures.isNotEmpty()) {
                    LocationState.ShowCultures(cultures)
                } else {
                    LocationState.AddCulture(location)
                }
            }
        }
    }

    private val <T : Any> ApiResponse.Failure<T>.errorMessage
        get() = when (this.message) {
            "DuplicateCulture" -> R.string.add_culture_duplicate
            else -> R.string.generic_sorry
        }
    private val <T : Any> ApiResponse.Exception<T>.errorMessage get() = R.string.generic_sorry
}