package uk.co.culturebook.add_new

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import uk.co.culturebook.add_new.data.TypeData
import uk.co.culturebook.add_new.info.composables.AddInfoRoute
import uk.co.culturebook.add_new.link_elements.LinkElementsRoute
import uk.co.culturebook.add_new.location.composables.LocationRoute
import uk.co.culturebook.add_new.submit.SubmitRoute
import uk.co.culturebook.add_new.title_type.composables.TitleAndTypeRoute
import uk.co.culturebook.nav.Route
import uk.co.culturebook.nav.navigateTop

fun NavGraphBuilder.addNewGraph(navController: NavController, viewModel: AddNewViewModel) {
    val addNewState = viewModel.state

    navigation(
        Route.AddNew.Location.route,
        Route.AddNew.route
    ) {
        composable(Route.AddNew.Location.route) {
            LocationRoute({ navController.navigateUp() }, onDone = { culture, location ->
                addNewState.culture = culture
                addNewState.location = location
                navController.navigate(Route.AddNew.TitleAndType.route)
            })
        }

        composable(Route.AddNew.TitleAndType.route) {
            TitleAndTypeRoute(
                { navController.navigateUp() },
                typeData = TypeData().apply {
                    name = addNewState.name
                    type = addNewState.type
                    parentElement = addNewState.parentElement
                },
                onElementAndTitleSelected = { typeData ->
                    addNewState.type = typeData.type
                    addNewState.name = typeData.name
                    addNewState.parentElement = typeData.parentElement
                    navController.navigate(Route.AddNew.AddInfo.Base.route)
                }
            )
        }

        navigation(Route.AddNew.AddInfo.Base.route, Route.AddNew.AddInfo.route) {
            composable(Route.AddNew.AddInfo.Base.route) {
                AddInfoRoute(
                    { navController.navigateUp() },
                    { navController.navigate(it) },
                    addNewState = addNewState
                )
            }
            composable(Route.AddNew.AddInfo.LinkElements.route) {
                val elements = addNewState.linkElements
                LinkElementsRoute(
                    navigateBack = { navController.navigateUp() },
                    elements = elements,
                    onSubmit = { addNewState.linkElements = it })
            }
        }

        composable(Route.AddNew.Review.route) {
            SubmitRoute(
                navigateBack = { navController.navigateUp() },
                addNewState = addNewState,
                onFinished = {
                    addNewState.clear()
                    navController.navigateTop(Route.Home)
                }
            )
        }
    }
}

