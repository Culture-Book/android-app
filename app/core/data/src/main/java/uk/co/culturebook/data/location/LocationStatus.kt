package uk.co.culturebook.data.location

import android.location.Location

sealed interface LocationStatus {
    object Fetching : LocationStatus
    data class Success(val location: Location) : LocationStatus
    object Error : LocationStatus
}