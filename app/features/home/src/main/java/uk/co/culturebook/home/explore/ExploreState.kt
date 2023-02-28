package uk.co.culturebook.home.explore

import uk.co.culturebook.data.models.cultural.Element

sealed interface ExploreState {
    object UserFetched : ExploreState
    object Success : ExploreState
    data class ElementsReceived(val elements: List<Element>) : ExploreState
    data class ElementsWithMediaReceived(val elements: List<Element>) : ExploreState
    object Idle : ExploreState
    object Loading : ExploreState
    sealed interface Error : ExploreState {
        object ToSUpdate : Error
        data class Generic(val stringId: Int) : Error
    }
}