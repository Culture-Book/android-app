package uk.co.culturebook.home.composables

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import uk.co.culturebook.home.explore.ExploreRoute
import uk.co.culturebook.nav.Route


fun NavGraphBuilder.homeGraph(navController: NavController) {
    navigation(
        startDestination = ExploreRoute.Explore.route,
        route = Route.Home.route
    ) {
        composable(ExploreRoute.Explore.route) {
            ExploreRoute(navController)
        }
        composable(ExploreRoute.Account.route) {
            AccountRoute(navController)
        }
    }
}
