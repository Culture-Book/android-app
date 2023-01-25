package uk.co.culturebook.add_new

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import uk.co.culturebook.add_new.location.composables.LocationRoute
import uk.co.culturebook.nav.Route

fun NavGraphBuilder.addNewGraph(navController: NavController) {
    navigation(
        Route.AddNew.Location.route,
        Route.AddNew.route
    ) {
        composable(Route.AddNew.Location.route) {
            LocationRoute(navController)
        }
    }
}

