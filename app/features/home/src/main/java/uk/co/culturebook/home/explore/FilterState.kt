package uk.co.culturebook.home.explore

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import uk.co.culturebook.data.models.cultural.ElementType

@Stable
class FilterState {
    var elementTypes by mutableStateOf<List<ElementType>>(emptyList())
    var sortBy by mutableStateOf(SortBy.Nearest)

    fun copy(
        types: List<ElementType> = elementTypes, sort: SortBy = sortBy
    ) = FilterState().apply {
        elementTypes = types
        sortBy = sort
    }
}

enum class SortBy {
    Nearest
}