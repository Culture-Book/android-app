package uk.co.culturebook.add_new

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import uk.co.culturebook.add_new.data.AddNewState
import uk.co.culturebook.add_new.data.TypeData
import uk.co.culturebook.add_new.info.composables.AddInfoRoute
import uk.co.culturebook.add_new.location.composables.LocationRoute
import uk.co.culturebook.add_new.submit.SubmitRoute
import uk.co.culturebook.add_new.title_type.composables.TitleAndTypeRoute
import uk.co.culturebook.nav.Route
import uk.co.culturebook.nav.Route.AddNew.AddInfo.LinkElements.linkedElementsParam
import uk.co.culturebook.nav.fromJsonString

fun NavGraphBuilder.addNewGraph(navController: NavController) {
    val addNewState = AddNewState()

    navigation(
        Route.AddNew.Location.route,
        Route.AddNew.route
    ) {
        composable(Route.AddNew.Location.route) {
            LocationRoute(navController, onDone = { culture, location ->
                addNewState.culture = culture
                addNewState.location = location
            })
        }

        composable(Route.AddNew.TitleAndType.route) {
            TitleAndTypeRoute(
                navController,
                typeData = TypeData().apply {
                    name = addNewState.name
                    type = addNewState.type
                    parentElement = addNewState.parentElement
                },
                onElementAndTitleSelected = { typeData ->
                    addNewState.type = typeData.type
                    addNewState.name = typeData.name
                    addNewState.parentElement = typeData.parentElement
                }
            )
        }

        navigation(Route.AddNew.AddInfo.Base.route, Route.AddNew.AddInfo.route) {
            composable(Route.AddNew.AddInfo.Base.route) {
                AddInfoRoute(
                    navController,
                    type = addNewState.type!!,
                    onDone = { infoData ->
                        addNewState.information = infoData.background
                        addNewState.linkElements = infoData.linkedElements
                        addNewState.files = infoData.files
                        addNewState.eventType = infoData.eventType
                    }
                )
            }
            composable(Route.AddNew.AddInfo.LinkElements.route + "{$linkedElementsParam}") {
                it.arguments?.getString(linkedElementsParam)?.fromJsonString<List<String>>()
                // TODO - add linking when nearby is done
            }
        }

        composable(Route.AddNew.Review.route) {
            SubmitRoute(
                navController = navController,
                addNewState = addNewState,
                onFinished = addNewState::clear
            )
        }
    }
}

