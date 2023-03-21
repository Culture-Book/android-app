package uk.co.culturebook.details

import androidx.annotation.StringRes
import uk.co.culturebook.data.models.cultural.Comment
import uk.co.culturebook.data.models.cultural.Contribution
import uk.co.culturebook.data.models.cultural.Element

sealed interface DetailState {
    object Idle : DetailState
    object Success : DetailState
    object Blocked : DetailState
    data class ElementCommentsReceived(val comments: List<Comment>) : DetailState
    data class ContributionCommentsReceived(val comments: List<Comment>) : DetailState
    data class ElementReceived(val element: Element) : DetailState
    data class ContributionReceived(val contribution: Contribution) : DetailState
    data class ContributionsReceived(val contributions: List<Contribution>) : DetailState
    object Loading : DetailState
    data class Error(@StringRes val error: Int) : DetailState
}