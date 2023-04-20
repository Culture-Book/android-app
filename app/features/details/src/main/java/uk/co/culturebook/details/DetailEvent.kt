package uk.co.culturebook.details

import uk.co.culturebook.data.models.cultural.Element
import java.util.UUID

sealed interface DetailEvent {
    object Idle : DetailEvent
    data class GetElement(val uuid: UUID?) : DetailEvent
    data class GetContribution(val uuid: UUID?) : DetailEvent
    data class GetContributions(val uuid: UUID?) : DetailEvent
    data class BlockElement(val uuid: UUID?) : DetailEvent
    data class BlockContribution(val uuid: UUID?) : DetailEvent
    data class FavouriteElement(val element: Element) : DetailEvent
    data class FavouriteContribution(val contributionId: UUID) : DetailEvent
    data class FavouriteFromShowContributions(val contributionId: UUID, val elementId: UUID) :
        DetailEvent

    data class BlockFromShowContributions(val contributionId: UUID, val elementId: UUID) :
        DetailEvent

    data class AddElementComment(val parentId: UUID, val comment: String) : DetailEvent
    data class ToggleElementReaction(val parentId: UUID, val reaction: String) : DetailEvent
    data class BlockElementComment(val elementId: UUID, val id: UUID) : DetailEvent
    data class DeleteElementComment(val elementId: UUID, val id: UUID) : DetailEvent
    data class AddContributionComment(val parentId: UUID, val comment: String) : DetailEvent
    data class ToggleContributionReaction(val contributionId: UUID, val reaction: String) :
        DetailEvent

    data class BlockContributionComment(val contributionId: UUID, val id: UUID) : DetailEvent
    data class DeleteContributionComment(val contributionId: UUID, val id: UUID) : DetailEvent
    data class GetElementComments(val elementId: UUID) : DetailEvent
    data class GetContributionComments(val contributionId: UUID) : DetailEvent
}