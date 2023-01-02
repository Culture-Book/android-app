package io.culturebook.home.navigation

import io.culturebook.ui.R
import io.culturebook.ui.theme.AppIcon

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

    object Social : ExploreRoute {
        override val selectedIcon: AppIcon = AppIcon.Social
        override val unselectedIcon: AppIcon = AppIcon.SocialOutline
        override val route: String = "social"
        override val labelId = R.string.social
    }
}