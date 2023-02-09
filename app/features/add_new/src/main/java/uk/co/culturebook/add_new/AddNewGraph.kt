package uk.co.culturebook.add_new

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import uk.co.culturebook.add_new.data.ElementState
import uk.co.culturebook.add_new.info.composables.AddInfoRoute
import uk.co.culturebook.add_new.location.composables.LocationRoute
import uk.co.culturebook.add_new.submit.SubmitRoute
import uk.co.culturebook.add_new.title_type.composables.TitleAndTypeRoute
import uk.co.culturebook.nav.Route
import uk.co.culturebook.nav.Route.AddNew.AddInfo.LinkElements.linkedElementsParam
import uk.co.culturebook.nav.fromJsonString

fun NavGraphBuilder.addNewGraph(navController: NavController) {
    val elementState = ElementState() // TODO - Save Configuration

    navigation(
        Route.AddNew.Location.route,
        Route.AddNew.route
    ) {
        composable(Route.AddNew.Location.route) {
            LocationRoute(navController, onDone = { culture, location ->
                elementState.culture = culture
                elementState.location = location
            })
        }

        composable(Route.AddNew.TitleAndType.route) {
            TitleAndTypeRoute(
                navController,
                elementState.type,
                elementState.name,
                onElementAndTitleSelected = { title, type ->
                    elementState.type = type
                    elementState.name = title
                }
            )
        }

        navigation(Route.AddNew.AddInfo.Base.route, Route.AddNew.AddInfo.route) {
            composable(Route.AddNew.AddInfo.Base.route) {
                AddInfoRoute(
                    navController,
                    type = elementState.type!!,
                    onDone = { infoData ->
                        elementState.information = infoData.background
                        elementState.linkElements = infoData.linkedElements
                        elementState.files = infoData.files
                        elementState.eventType = infoData.eventType
                    }
                )
            }
            composable(Route.AddNew.AddInfo.LinkElements.route + "{$linkedElementsParam}") {
                val elements =
                    it.arguments?.getString(linkedElementsParam)?.fromJsonString<List<String>>()
                // TODO - add linking when nearby is done
            }
        }

        composable(Route.AddNew.Review.route) {
            SubmitRoute(navController = navController, element = elementState)
        }
    }
}

