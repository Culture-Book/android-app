package uk.co.culturebook.add_new.title_type.composables

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.co.culturebook.add_new.data.TypeData
import uk.co.culturebook.add_new.title_type.TitleAndTypeViewModel
import uk.co.culturebook.add_new.title_type.events.TitleAndTypeEvent
import uk.co.culturebook.add_new.title_type.states.TitleAndTypeState
import uk.co.culturebook.data.repositories.cultural.AddNewRepository

@Composable
fun TitleAndTypeRoute(
    navigateBack: () -> Unit,
    typeData: TypeData,
    onElementAndTitleSelected: (TypeData) -> Unit
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
            onElementAndTitleSelected(success.typeData)
            viewModel.postEvent(TitleAndTypeEvent.Idle)
        }
    }

    TitleAndTypeScreen(
        state,
        _typeData = typeData,
        onBack = navigateBack,
        postEvent = viewModel::postEvent
    )
}