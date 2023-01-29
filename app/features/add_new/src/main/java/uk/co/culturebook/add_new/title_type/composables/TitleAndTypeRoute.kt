package uk.co.culturebook.add_new.title_type.composables

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import uk.co.culturebook.add_new.title_type.TitleAndTypeViewModel
import uk.co.culturebook.add_new.title_type.events.TitleAndTypeEvent
import uk.co.culturebook.add_new.title_type.states.TitleAndTypeState
import uk.co.culturebook.data.models.cultural.ElementType
import uk.co.culturebook.data.repositories.cultural.AddNewRepository
import uk.co.culturebook.nav.Route

@Composable
fun TitleAndTypeRoute(
    navController: NavController,
    onElementAndTitleSelected: (ElementType, String) -> Unit
) {
    val viewModel = viewModel {
        val addNewRepository =
            AddNewRepository((this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application))
        TitleAndTypeViewModel(addNewRepository)
    }
    val state by viewModel.titleAndTypeState.collectAsState()

    LaunchedEffect(state) {
        val success = state as? TitleAndTypeState.Success
        if (success != null) {
            onElementAndTitleSelected(success.elementType, success.title)
            navController.navigate(Route.AddNew.AddInfo.route)
            viewModel.postEvent(TitleAndTypeEvent.Idle)
        }
    }

    TitleAndTypeScreen(
        state,
        onBack = { navController.navigateUp() },
        postEvent = viewModel::postEvent)
}