package uk.co.culturebook.data.models.cultural

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.serialization.Serializable
import uk.co.culturebook.data_access.serializers.UUIDSerializer
import java.util.*

@Stable
class SearchCriteriaState {
    var location: Location? by mutableStateOf(null)
    var searchString: String? by mutableStateOf(null)
    var elementId: UUID? by mutableStateOf(null)
    var contributionId: UUID? by mutableStateOf(null)
    var types: List<ElementType> by mutableStateOf(allElementTypes)
    var page: Int by mutableStateOf(1)
    var radius: Double by mutableStateOf(10.0)

    fun copy(
        _location: Location? = location,
        _searchString: String? = searchString,
        _elementId: UUID? = elementId,
        _contributionId: UUID? = contributionId,
        _types: List<ElementType> = types,
        _page: Int = page,
        _radius: Double = radius
    ) = SearchCriteriaState().apply {
        location = _location
        searchString = _searchString
        elementId = _elementId
        contributionId = _contributionId
        types = _types
        page = _page
        radius = _radius
    }

    fun apply(searchCriteriaState: SearchCriteriaState) {
        location = searchCriteriaState.location
        searchString = searchCriteriaState.searchString
        elementId = searchCriteriaState.elementId
        contributionId = searchCriteriaState.contributionId
        types = searchCriteriaState.types
        page = searchCriteriaState.page
        radius = searchCriteriaState.radius
    }

    fun toSearchCriteria() = SearchCriteria(
        location = location,
        searchString = searchString,
        elementId = elementId,
        contributionId = contributionId,
        types = types,
        page = page,
        radius = radius
    )
}

fun SearchCriteriaState.isValid() =
    (location != null || !searchString.isNullOrEmpty()) && page > 0 && radius > 0.0