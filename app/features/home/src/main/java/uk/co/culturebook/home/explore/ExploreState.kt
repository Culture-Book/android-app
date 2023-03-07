package uk.co.culturebook.home.explore

import uk.co.culturebook.data.models.cultural.Contribution
import uk.co.culturebook.data.models.cultural.Culture
import uk.co.culturebook.data.models.cultural.Element

sealed interface ExploreState {
    sealed interface Success : ExploreState {
        object UserFetched : Success
        data class ElementsReceived(val elements: List<Element>) : Success
        data class ContributionsReceived(val contributions: List<Contribution>) : Success
        data class CulturesReceived(val cultures: List<Culture>) : Success
    }

    data class Navigate(val route: String) : ExploreState
    object GetElements : ExploreState
    object GetContributions : ExploreState
    object GetCultures : ExploreState
    object Idle : ExploreState
    object Loading : ExploreState
    sealed interface Error : ExploreState {
        object ToSUpdate : Error
        data class Generic(val stringId: Int) : Error
    }
}