package uk.co.culturebook.add_new.location.events

import uk.co.culturebook.data.models.cultural.Culture
import uk.co.culturebook.data.models.cultural.Location

sealed interface LocationEvent {
    object ShowMap : LocationEvent
    data class SelectCulture(val culture: Culture, val location: Location) : LocationEvent
    data class AddCulture(val cultureName: String, val location: Location) : LocationEvent
    data class AddCultureRequest(val location: Location) : LocationEvent
    data class GetCultures(val location: Location) : LocationEvent
}