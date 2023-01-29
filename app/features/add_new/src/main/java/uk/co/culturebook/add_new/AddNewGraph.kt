package uk.co.culturebook.add_new

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import uk.co.culturebook.add_new.info.composables.AddInfoRoute
import uk.co.culturebook.add_new.location.composables.LocationRoute
import uk.co.culturebook.add_new.title_type.composables.TitleAndTypeRoute
import uk.co.culturebook.nav.Route

fun NavGraphBuilder.addNewGraph(navController: NavController) {
    navigation(
        Route.AddNew.Location.route,
        Route.AddNew.route
    ) {
        composable(Route.AddNew.Location.route) {
            // Get a new view model and attach the current nav backstack entry as its lifecycle
            val viewModel: AddNewViewModel = viewModel(it)
            LocationRoute(navController, onCultureSelected = viewModel::registerCulture)
        }

        composable(Route.AddNew.TitleAndType.route) {
            // Get the view model attached to the previous entry
            val viewModel: AddNewViewModel = viewModel(navController.previousBackStackEntry!!)
            TitleAndTypeRoute(
                navController,
                onElementAndTitleSelected = viewModel::registerTitleAndType
            )
        }

        composable(Route.AddNew.AddInfo.route) {
            // Get the view model attached to the previous entry
            val viewModel: AddNewViewModel = viewModel(navController.previousBackStackEntry!!)
            AddInfoRoute(
                navController,
                viewModel::registerInfo
            )
        }
    }
}

