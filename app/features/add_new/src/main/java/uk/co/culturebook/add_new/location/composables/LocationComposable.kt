package uk.co.culturebook.add_new.location.composables

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.maps.android.compose.rememberCameraPositionState
import uk.co.common.AskForLocationPermission
import uk.co.common.choose_location.LocationBody
import uk.co.culturebook.add_new.location.LocationViewModel
import uk.co.culturebook.add_new.location.composables.add_new_culture.AddNewCulture
import uk.co.culturebook.add_new.location.composables.show_cultures.ShowCultures
import uk.co.culturebook.add_new.location.events.LocationEvent
import uk.co.culturebook.add_new.location.states.LocationState
import uk.co.culturebook.data.location.*
import uk.co.culturebook.data.models.cultural.*
import uk.co.culturebook.data.repositories.cultural.AddNewRepository
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.*
import uk.co.culturebook.ui.theme.molecules.LoadingComposable
import uk.co.culturebook.ui.utils.ShowSnackbar

@Composable
fun LocationRoute(navigateBack: () -> Unit, onDone: (Culture, Location) -> Unit) {
    val viewModel = viewModel {
        val addNewRepository =
            AddNewRepository((this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application))
        LocationViewModel(addNewRepository)
    }
    val state by viewModel.locationState.collectAsState()

    AskForLocationPermission()

    LocationScreen(
        onBack = navigateBack,
        state = state,
        viewModel::postEvent
    ) { culture, location ->
        onDone(culture, location)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(
    onBack: () -> Unit,
    state: LocationState,
    postEvent: (LocationEvent) -> Unit,
    onDone: (Culture, Location) -> Unit
) {
    val snackbarState = remember { SnackbarHostState() }
    val cameraPositionState = rememberCameraPositionState()
    val userLatLng by remember { derivedStateOf { cameraPositionState.position.target } }

    if (state is LocationState.Error) {
        ShowSnackbar(
            stringId = state.errorId,
            onShow = { postEvent(LocationEvent.ShowMap) },
            snackbarState = snackbarState
        )
    }

    Scaffold(
        topBar = { LocationAppBar(onBack) },
        snackbarHost = { SnackbarHost(snackbarState) }) { padding ->
        when (state) {
            is LocationState.Loading -> LoadingComposable(padding)
            is LocationState.AddCulture -> {
                AddNewCulture(
                    modifier = Modifier
                        .padding(padding)
                        .padding(horizontal = mediumSize),
                    location = userLatLng.location,
                    postEvent = postEvent
                )
            }

            is LocationState.SelectedCulture -> {
                DisposableEffect(state) {
                    onDone(state.culture, state.location)
                    onDispose {
                        postEvent(LocationEvent.ShowMap)
                    }
                }
            }

            is LocationState.ShowCultures -> {
                ShowCultures(
                    modifier = Modifier
                        .padding(padding)
                        .padding(horizontal = mediumSize),
                    cultures = state.cultures,
                    onCultureSelected = {
                        postEvent(
                            LocationEvent.SelectCulture(
                                it,
                                cameraPositionState.position.target.location
                            )
                        )
                    },
                    onAddNewCultureClicked = {
                        postEvent(LocationEvent.AddCultureRequest(location = userLatLng.location))
                    },
                    onSelectLocation = {
                        postEvent(LocationEvent.ShowMap)
                    }
                )
            }

            else -> {
                LocationBody(
                    modifier = Modifier
                        .padding(padding)
                        .padding(mediumSize),
                    title = stringResource(R.string.location_title),
                    subtitle = stringResource(R.string.location_subtitle),
                    onLocationSelected = { postEvent(LocationEvent.GetCultures(it)) },
                    cameraPositionState = cameraPositionState
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationAppBar(navigateBack: () -> Unit = {}) {
    TopAppBar(modifier = Modifier.fillMaxWidth(),
        title = { Text(stringResource(R.string.add_new)) },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    painter = AppIcon.Close.getPainter(), contentDescription = "navigate back"
                )
            }
        })
}