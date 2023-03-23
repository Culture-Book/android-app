package uk.co.culturebook.explore

import uk.co.culturebook.data.models.cultural.Contribution
import uk.co.culturebook.data.models.cultural.Element
import java.util.*

sealed interface ExploreEvent {
    object Idle : ExploreEvent
    object GetUser : ExploreEvent
    object GetElements : ExploreEvent
    object GetContributions : ExploreEvent
    object GetCultures : ExploreEvent
    data class GoToElementDetails(val element: Element) : ExploreEvent
    data class GoToContributionDetails(val contribution: Contribution) : ExploreEvent
    data class BlockElement(val uuid: UUID?) : ExploreEvent
    data class BlockCulture(val uuid: UUID?) : ExploreEvent
    data class BlockContribution(val uuid: UUID?) : ExploreEvent
    data class FavouriteElement(val uuid: UUID?) : ExploreEvent
    data class FavouriteCulture(val uuid: UUID?) : ExploreEvent
    data class FavouriteContribution(val uuid: UUID?) : ExploreEvent
    object UpdateToS : ExploreEvent
    sealed interface Error : ExploreEvent {
        object ToSUpdate : Error
        data class Generic(val stringId: Int) : Error
    }
}