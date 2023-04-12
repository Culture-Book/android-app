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
    private var _location: Location? by mutableStateOf(null)
    private var _getFavourites: Boolean by mutableStateOf(false)
    private var _searchString: String? by mutableStateOf(null)
    private var _elementId: UUID? by mutableStateOf(null)
    private var _contributionId: UUID? by mutableStateOf(null)
    private var _searchType: SearchType by mutableStateOf(SearchType.Element)
    private var _types: List<ElementType> by mutableStateOf(allElementTypes)
    private var _page: Int by mutableStateOf(1)
    private var _radius: Double by mutableStateOf(10.0)

    var location
        get() = _location
        set(value) {
            _location = value
        }
    var getFavourites
        get() = _getFavourites
        set(value) {
            _getFavourites = value
        }

    var searchString
        get() = _searchString
        set(value) {
            _searchString = value
        }

    var elementId
        get() = _elementId
        set(value) {
            _elementId = value
        }

    var contributionId
        get() = _contributionId
        set(value) {
            _contributionId = value
        }

    // When the search type changes we reset the page to 1 in order to start a new search, otherwise we'll just fetch the page of the previous search
    var searchType
        get() = _searchType
        set(value) {
            _page = 1
            _searchType = value
        }

    var types
        get() = _types
        set(value) {
            _types = value
        }

    var page
        get() = _page
        set(value) {
            _page = value
        }

    var radius
        get() = _radius
        set(value) {
            _radius = value
        }


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
        _getFavourites: Boolean = getFavourites,
        _searchString: String? = searchString,
        _elementId: UUID? = elementId,
        _contributionId: UUID? = contributionId,
        _types: List<ElementType> = types,
        _page: Int = page,
        _radius: Double = radius,
        _searchType: SearchType = searchType
    ) = SearchCriteriaState().apply {
        location = _location
        getFavourites = _getFavourites
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
        getFavourites = searchCriteriaState.getFavourites
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