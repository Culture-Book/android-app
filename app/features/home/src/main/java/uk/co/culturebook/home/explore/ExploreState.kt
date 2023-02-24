package uk.co.culturebook.home.explore

import androidx.annotation.StringRes
import uk.co.culturebook.ui.theme.molecules.BannerType

sealed interface ExploreState {
    object UserFetched : ExploreState
    object Success : ExploreState
    object Idle : ExploreState
    object Loading : ExploreState
    sealed interface Error : ExploreState {
        object ToSUpdate : Error
        data class Generic(val stringId: Int) : Error
    }
}