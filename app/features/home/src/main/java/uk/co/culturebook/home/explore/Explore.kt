package uk.co.culturebook.home.explore

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import uk.co.common.RegisterLocationChanges
import uk.co.culturebook.data.flows.EventBus
import uk.co.culturebook.data.location.LocationFlow
import uk.co.culturebook.data.location.LocationStatus
import uk.co.culturebook.data.models.cultural.SearchCriteriaState
import uk.co.culturebook.data.models.cultural.isValid
import uk.co.culturebook.data.repositories.authentication.UserRepository
import uk.co.culturebook.data.repositories.cultural.ElementsRepository
import uk.co.culturebook.nav.Route
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.molecules.LoadingComposable
import uk.co.culturebook.ui.utils.ShowSnackbar

@Composable
fun ExploreRoute(navController: NavController) {
    val viewModel = viewModel {
        val userRepository =
            UserRepository((this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application))
        val elementsRepository =
            ElementsRepository((this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application))
        ExploreViewModel(userRepository = userRepository, elementsRepository = elementsRepository)
    }
    val searchCriteriaState = viewModel.searchCriteriaState
    val nearbyState by viewModel.exploreState.collectAsState()
    val locationStatus by LocationFlow.collectAsState()

    LaunchedEffect(locationStatus) {
        val location = (locationStatus as? LocationStatus.Success)?.location
        if (location != null) {
            searchCriteriaState.location = location
        }
    }

    LaunchedEffect(
        searchCriteriaState.location,
        searchCriteriaState.searchString,
        searchCriteriaState.page,
        searchCriteriaState.radius,
        searchCriteriaState.types
    ) {
        if (searchCriteriaState.isValid()) {
            viewModel.postEvent(ExploreEvent.GetElements(searchCriteriaState))
        }
    }

    RegisterLocationChanges()

    Explore(navController, searchCriteriaState, nearbyState, viewModel::postEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Explore(
    navController: NavController,
    searchCriteriaState: SearchCriteriaState,
    exploreState: ExploreState,
    postEvent: (ExploreEvent) -> Unit
) {
    val snackbarState = remember { SnackbarHostState() }
    val showToSDialog by remember { derivedStateOf { exploreState is ExploreState.Error.ToSUpdate } }
    var showFiltersDialog by remember { mutableStateOf(false) }

    if (showToSDialog) {
        ToSDialog(
            onCancel = { EventBus.logout() },
            onAccept = { postEvent(ExploreEvent.UpdateToS) },
            onTosClicked = { navController.navigate(Route.WebView.ToS.route) },
            onPrivacyClicked = { navController.navigate(Route.WebView.Privacy.route) }
        )
    }

    if (showFiltersDialog) {
        FilterSheet(
            searchCriteriaState = searchCriteriaState,
            onDismiss = { showFiltersDialog = false },
            onConfirm = {
                showFiltersDialog = false
                searchCriteriaState.apply(it)
            }
        )
    }

    if (exploreState is ExploreState.Error.Generic) {
        ShowSnackbar(stringId = exploreState.stringId, snackbarState = snackbarState, onShow = {
            postEvent(ExploreEvent.Idle)
        })
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarState) },
        topBar = { SearchAppBar(onFiltersClicked = { showFiltersDialog = true }) }
    ) { padding ->
        when (exploreState) {
            ExploreState.UserFetched,
            ExploreState.Loading -> LoadingComposable(padding)
            else -> ExploreBody(
                Modifier.padding(padding),
                exploreState,
                searchCriteriaState,
                postEvent
            )
        }
    }
}

@Composable
fun ToSDialog(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onAccept: () -> Unit,
    onTosClicked: (() -> Unit)? = null,
    onPrivacyClicked: (() -> Unit)? = null
) {
    var accept by remember { mutableStateOf(false) }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { if (accept) onCancel() else onAccept() },
        confirmButton =
        {
            TextButton(onClick = { accept = true; onAccept() }) {
                Text(stringResource(R.string.accept))
            }
        },
        dismissButton = {
            TextButton(onClick = { accept = false; onCancel() }) {
                Text(stringResource(R.string.logout))
            }
        },
        title = { Text(stringResource(R.string.tos_update_title)) },
        text = {
            Column(verticalArrangement = Arrangement.SpaceEvenly) {
                Text(stringResource(R.string.tos_update))

                Text(
                    modifier = Modifier
                        .clickable { onTosClicked?.invoke() },
                    text = stringResource(R.string.read_tos),
                    textDecoration = if (onTosClicked != null) TextDecoration.Underline else TextDecoration.None
                )

                Text(
                    modifier = Modifier
                        .clickable { onPrivacyClicked?.invoke() },
                    text = stringResource(R.string.read_privacy),
                    textDecoration = if (onPrivacyClicked != null) TextDecoration.Underline else TextDecoration.None
                )
            }


        }
    )
}