package uk.co.culturebook.data.repositories.cultural

import android.content.Context
import uk.co.culturebook.data.Singletons
import uk.co.culturebook.data.models.cultural.Comment
import uk.co.culturebook.data.models.cultural.Reaction
import uk.co.culturebook.data.models.cultural.RequestComment
import uk.co.culturebook.data.models.cultural.RequestReaction
import java.util.*

class DetailsRepository(context: Context) {
    private val apiInterface = Singletons.getApiInterface(context)

    suspend fun getElementComments(elementId: UUID) = apiInterface.getElementComments(elementId)

    suspend fun getContributionComments(contributionId: UUID) =
        apiInterface.getContributionComments(contributionId)

    suspend fun addElementComment(elementId: UUID, comment: String) =
        apiInterface.addElementComment(
            RequestComment(
                elementId,
                null,
                Comment(comment = comment)
            )
        )

    suspend fun addContributionComment(contributionId: UUID, comment: String) =
        apiInterface.addContributionComment(
            RequestComment(
                null,
                contributionId,
                Comment(comment = comment)
            )
        )

    suspend fun blockElementComment(elementId: UUID, commentId: UUID) =
        apiInterface.blockElementComment(
            RequestComment(
                elementId,
                null,
                Comment(commentId)
            )
        )


    suspend fun blockContributionComment(contributionId: UUID, commentId: UUID) =
        apiInterface.blockContributionComment(
            RequestComment(
                null,
                contributionId,
                Comment(commentId)
            )
        )


    suspend fun deleteElementComment(commentId: UUID) =
        apiInterface.deleteElementComment(commentId = commentId)


    suspend fun deleteContributionComment(commentId: UUID) =
        apiInterface.deleteContributionComment(commentId = commentId)

    suspend fun toggleElementReaction(elementId: UUID, reaction: String) =
        apiInterface.toggleElementReaction(
            RequestReaction(
                elementId,
                null,
                Reaction(reaction = reaction)
            )
        )


    suspend fun toggleContributionReaction(contributionId: UUID, reaction: String) =
        apiInterface.toggleContributionReaction(
            RequestReaction(
                null,
                contributionId,
                Reaction(reaction = reaction)
            )
        )
}