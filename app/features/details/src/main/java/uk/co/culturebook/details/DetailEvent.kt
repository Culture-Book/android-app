package uk.co.culturebook.details

import uk.co.culturebook.data.models.cultural.Contribution
import uk.co.culturebook.data.models.cultural.Element
import java.util.UUID

sealed interface DetailEvent {
    object Idle : DetailEvent
    data class GetElement(val uuid: UUID) : DetailEvent
    data class GetContribution(val uuid: UUID) : DetailEvent
    data class BlockElement(val uuid: UUID?) : DetailEvent
    data class BlockContribution(val uuid: UUID?) : DetailEvent
    data class FavouriteElement(val element: Element) : DetailEvent
    data class FavouriteContribution(val contribution: Contribution) : DetailEvent
}