package uk.co.culturebook.details

import uk.co.culturebook.data.models.cultural.Contribution
import uk.co.culturebook.data.models.cultural.Element
import java.util.*

sealed interface DetailEvent {
    object Idle : DetailEvent
    data class GetElement(val uuid: UUID?) : DetailEvent
    data class GetContribution(val uuid: UUID?) : DetailEvent
    data class GetContributions(val uuid: UUID?) : DetailEvent
    data class BlockElement(val uuid: UUID?) : DetailEvent
    data class BlockContribution(val uuid: UUID?) : DetailEvent
    data class FavouriteElement(val element: Element) : DetailEvent
    data class FavouriteContribution(val contribution: Contribution) : DetailEvent
    data class AddComment(val parentId: UUID, val comment: String) : DetailEvent
    data class AddReaction(val reaction: String) : DetailEvent
    data class BlockComment(val id: UUID) : DetailEvent
}