package uk.co.culturebook.data.models.cultural

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.*

@kotlinx.serialization.Serializable
enum class SearchType {
    Culture, Element, Contribution
}

@Stable
class SearchCriteriaState {
    var location: Location? by mutableStateOf(null)
    var searchString: String? by mutableStateOf(null)
    var elementId: UUID? by mutableStateOf(null)
    var contributionId: UUID? by mutableStateOf(null)
    var searchType: SearchType by mutableStateOf(SearchType.Element)
    var types: List<ElementType> by mutableStateOf(allElementTypes)
    var page: Int by mutableStateOf(1)
    var radius: Double by mutableStateOf(10.0)

    fun toggleTypesSelection(elementType: ElementType) {
        val types = types.toMutableList()
        if (types.contains(elementType)) {
            types -= elementType
        } else {
            types += elementType
        }
        this.types = types
    }

    fun copy(
        _location: Location? = location,
        _searchString: String? = searchString,
        _elementId: UUID? = elementId,
        _contributionId: UUID? = contributionId,
        _types: List<ElementType> = types,
        _page: Int = page,
        _radius: Double = radius,
        _searchType: SearchType = searchType
    ) = SearchCriteriaState().apply {
        location = _location
        searchString = _searchString
        elementId = _elementId
        contributionId = _contributionId
        types = _types
        page = _page
        radius = _radius
        searchType = _searchType
    }

    fun apply(searchCriteriaState: SearchCriteriaState) {
        location = searchCriteriaState.location
        searchString = searchCriteriaState.searchString
        elementId = searchCriteriaState.elementId
        contributionId = searchCriteriaState.contributionId
        types = searchCriteriaState.types
        page = searchCriteriaState.page
        radius = searchCriteriaState.radius
        searchType = searchCriteriaState.searchType
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

fun SearchCriteriaState.isValid() = page > 0 && radius > 0.0

fun SearchCriteriaState.isValidElementSearch() = isValid() && searchType == SearchType.Element
fun SearchCriteriaState.isValidContributionSearch() =
    isValid() && searchType == SearchType.Contribution

fun SearchCriteriaState.isValidCultureSearch() =
    page > 0 && radius > 0.0 && searchType == SearchType.Culture