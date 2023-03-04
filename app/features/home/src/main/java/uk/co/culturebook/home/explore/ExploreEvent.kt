package uk.co.culturebook.home.explore

import uk.co.culturebook.data.models.cultural.SearchCriteriaState
import java.util.UUID

sealed interface ExploreEvent {
    object Idle : ExploreEvent
    object GetUser : ExploreEvent
    object GetElements : ExploreEvent
    data class GetElementsWithSearch(val searchCriteriaState: SearchCriteriaState) : ExploreEvent
    data class GetContributionsWithSearch(val searchCriteriaState: SearchCriteriaState) : ExploreEvent
    data class GetCulturesWithSearch(val searchCriteriaState: SearchCriteriaState) : ExploreEvent
    data class BlockElement(val uuid: UUID?) : ExploreEvent
    data class BlockCulture(val uuid: UUID?) : ExploreEvent
    data class BlockContribution(val uuid: UUID?) : ExploreEvent
    object UpdateToS : ExploreEvent
    sealed interface Error : ExploreEvent {
        object ToSUpdate : Error
        data class Generic(val stringId: Int) : Error
    }
}