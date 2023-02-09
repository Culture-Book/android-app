package uk.co.culturebook.data.location

import uk.co.culturebook.data.models.cultural.Location

sealed interface LocationStatus {
    object Fetching : LocationStatus
    data class Success(private val _location: android.location.Location) : LocationStatus {
        val location get() = Location(_location.latitude, _location.longitude)
    }

    object Error : LocationStatus
}