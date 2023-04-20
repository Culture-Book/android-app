package uk.co.culturebook.account.elements

import androidx.annotation.StringRes
import uk.co.culturebook.data.models.cultural.Contribution
import uk.co.culturebook.data.models.cultural.Culture
import uk.co.culturebook.data.models.cultural.Element
import uk.co.culturebook.ui.R
import java.util.UUID

sealed interface ElementsState {
    object Loading : ElementsState
    object Idle : ElementsState
    data class DeleteElement(val uuid: UUID) : ElementsState
    data class DeleteContribution(val uuid: UUID) : ElementsState
    data class DeleteCulture(val uuid: UUID) : ElementsState
    data class Error(@StringRes val messageId: Int = R.string.generic_sorry) : ElementsState
    data class ElementsFetched(val elements: List<Element>) : ElementsState
    data class ContributionsFetched(val contributions: List<Contribution>) : ElementsState
    data class CulturesFetched(val cultures: List<Culture>) : ElementsState
    data class FavouriteElementsFetched(val elements: List<Element>) : ElementsState
    data class FavouriteContributionsFetched(val contributions: List<Contribution>) : ElementsState
    data class FavouriteCulturesFetched(val cultures: List<Culture>) : ElementsState
}