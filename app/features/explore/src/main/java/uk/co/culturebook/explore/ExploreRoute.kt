package uk.co.culturebook.explore

import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppIcon

sealed interface ExploreRoute {
    val route: String
    val labelId: Int
    val selectedIcon: AppIcon
    val unselectedIcon: AppIcon

    object Explore : ExploreRoute {
        override val selectedIcon: AppIcon = AppIcon.MagnifyGlass
        override val unselectedIcon: AppIcon = AppIcon.MagnifyGlassOutline
        override val route: String = "explore"
        override val labelId = R.string.explore
    }

    object Account : ExploreRoute {
        override val selectedIcon: AppIcon = AppIcon.Account
        override val unselectedIcon: AppIcon = AppIcon.AccountOutline
        override val route: String = "account"
        override val labelId = R.string.account
    }
}