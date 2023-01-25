package uk.co.culturebook.add_new.location.composables

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.*
import com.google.maps.android.compose.rememberCameraPositionState
import uk.co.culturebook.add_new.location.LocationViewModel
import uk.co.culturebook.add_new.location.composables.add_new_culture.AddNewCulture
import uk.co.culturebook.add_new.location.composables.choose_location.LocationBody
import uk.co.culturebook.add_new.location.composables.choose_location.PermissionWrapper
import uk.co.culturebook.add_new.location.composables.show_cultures.ShowCultures
import uk.co.culturebook.add_new.location.events.LocationEvent
import uk.co.culturebook.add_new.location.states.LocationState
import uk.co.culturebook.data.location.*
import uk.co.culturebook.data.models.cultural.*
import uk.co.culturebook.data.repositories.cultural.AddNewRepository
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationRoute(navController: NavController) {
    val viewModel = viewModel {
        val addNewRepository =
            AddNewRepository((this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application))
        LocationViewModel(addNewRepository)
    }

    val permissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
    var showPermissionPrompt by remember { mutableStateOf(!permissionState.status.isGranted) }
    val context = LocalContext.current
    val state by viewModel.locationState.collectAsState()

    SystemBarsColors(MaterialTheme.colorScheme.background)

    if (showPermissionPrompt) {
        PermissionWrapper(
            permissionState = permissionState,
            onDenied = { showPermissionPrompt = false })
    }

    DisposableEffect(permissionState.status.isGranted) {
        if (permissionState.status.isGranted) {
            registerForLocationUpdates(context)
        }

        onDispose {
            unregisterLocationUpdates(context)
        }
    }

    LocationScreen(onBack = { navController.navigateUp() }, state = state, viewModel::postEvent, {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(
    onBack: () -> Unit,
    state: LocationState,
    postEvent: (LocationEvent) -> Unit,
    onCultureSelected: (Culture) -> Unit
) {
    val snackbarState = remember { SnackbarHostState() }
    val cameraPositionState = rememberCameraPositionState()
    val userLatLng by remember { derivedStateOf { cameraPositionState.position.target } }

    if (state is LocationState.Error) {
        val string = stringResource(state.errorId)
        LaunchedEffect(Unit) {
            snackbarState.showSnackbar(string)
        }
    }

    Scaffold(
        topBar = { LocationAppBar(onBack) },
        snackbarHost = { SnackbarHost(snackbarState) }) { padding ->
        when (state) {
            is LocationState.Loading -> {
                BoxWithConstraints(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) { CircularProgressIndicator(modifier = Modifier.align(Alignment.Center)) }
            }
            is LocationState.AddCulture -> {
                AddNewCulture(
                    modifier = Modifier
                        .padding(padding)
                        .padding(horizontal = mediumPadding),
                    location = userLatLng.toLocation(),
                    postEvent = postEvent
                )
            }
            is LocationState.SelectedCulture -> {
                LaunchedEffect(Unit) { snackbarState.showSnackbar("Culture selected: ${state.culture.name}") }
                onCultureSelected(state.culture)
            }
            is LocationState.ShowCultures -> {
                ShowCultures(
                    modifier = Modifier
                        .padding(padding)
                        .padding(horizontal = mediumPadding),
                    cultures = state.cultures,
                    onCultureSelected = { postEvent(LocationEvent.SelectCulture(it)) },
                    onAddNewCultureClicked = {
                        postEvent(LocationEvent.AddCultureRequest(location = userLatLng.toLocation()))
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
                        .padding(mediumPadding),
                    postEvent = postEvent,
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