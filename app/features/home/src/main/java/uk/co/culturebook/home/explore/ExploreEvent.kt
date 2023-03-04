package uk.co.culturebook.home.explore

import uk.co.culturebook.data.models.cultural.SearchCriteriaState

sealed interface ExploreEvent {
    object Idle : ExploreEvent
    object GetUser : ExploreEvent
    data class GetElements(val searchCriteriaState: SearchCriteriaState) : ExploreEvent
    data class GetContributions(val searchCriteriaState: SearchCriteriaState) : ExploreEvent
    data class GetCultures(val searchCriteriaState: SearchCriteriaState) : ExploreEvent
    object UpdateToS : ExploreEvent
    sealed interface Error : ExploreEvent {
        object ToSUpdate : Error
        data class Generic(val stringId: Int) : Error
    }
}