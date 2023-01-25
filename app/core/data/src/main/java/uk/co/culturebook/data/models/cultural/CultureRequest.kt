package uk.co.culturebook.data.models.cultural

import kotlinx.serialization.Serializable

@Serializable
data class CultureRequest(
    val culture: Culture,
    val location: Location
)
