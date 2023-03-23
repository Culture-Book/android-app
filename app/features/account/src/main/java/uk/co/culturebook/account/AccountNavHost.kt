package uk.co.culturebook.account

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import uk.co.culturebook.account.about.AboutRoute
import uk.co.culturebook.account.elements.ElementsRoute
import uk.co.culturebook.account.favourites.FavouritesRoute
import uk.co.culturebook.account.profile.ProfileRoute
import uk.co.culturebook.account.settings.SettingsRoute
import uk.co.culturebook.nav.Route


fun NavGraphBuilder.accountGraph(navController: NavController) {
    navigation(
        Route.Account.Home.route,
        Route.Account.route
    ) {
        composable(Route.Account.Home.route) { AccountRoute(navController) }

        composable(Route.Account.Profile.route) { ProfileRoute(navController) }

        composable(Route.Account.Elements.route) { ElementsRoute(navController) }

        composable(Route.Account.Favourites.route) { FavouritesRoute(navController) }

        composable(Route.Account.About.route) { AboutRoute(navController) }

        composable(Route.Account.Settings.route) { SettingsRoute(navController) }
    }
}