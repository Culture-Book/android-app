package uk.co.culturebook.add_new.location.states

import androidx.annotation.StringRes
import uk.co.culturebook.data.models.cultural.Culture
import uk.co.culturebook.data.models.cultural.Location

sealed interface LocationState {
    object ShowMap : LocationState
    object Loading : LocationState
    data class AddCulture(val location: Location) : LocationState
    data class ShowCultures(val cultures: List<Culture>) : LocationState
    data class SelectedCulture(val culture: Culture, val location: Location) : LocationState
    data class Error(@StringRes val errorId: Int) : LocationState
}