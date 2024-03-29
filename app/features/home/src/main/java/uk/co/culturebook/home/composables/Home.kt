package uk.co.culturebook.home.composables

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import uk.co.culturebook.account.accountGraph
import uk.co.culturebook.explore.ExploreRoute
import uk.co.culturebook.nav.Route


fun NavGraphBuilder.homeGraph(navController: NavController) {
    navigation(
        startDestination = ExploreRoute.Explore.route,
        route = Route.Home.route
    ) {
        accountGraph(navController)
        composable(ExploreRoute.Explore.route) {
            ExploreRoute { navController.navigate(it) }
        }
    }
}
