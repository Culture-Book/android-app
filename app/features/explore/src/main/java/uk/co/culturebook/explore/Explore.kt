package uk.co.culturebook.explore

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.co.common.RegisterLocationChanges
import uk.co.culturebook.data.flows.EventBus
import uk.co.culturebook.data.location.LocationFlow
import uk.co.culturebook.data.location.LocationStatus
import uk.co.culturebook.data.models.cultural.*
import uk.co.culturebook.data.repositories.authentication.UserRepository
import uk.co.culturebook.data.repositories.cultural.ElementsRepository
import uk.co.culturebook.data.repositories.cultural.UpdateRepository
import uk.co.culturebook.nav.Route
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppIcon
import uk.co.culturebook.ui.theme.molecules.LoadingComposable
import uk.co.culturebook.ui.utils.ShowSnackbar

@Composable
fun ExploreRoute(navigate: (String) -> Unit) {
    val viewModel = viewModel {
        val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
        val userRepository = UserRepository(app)
        val elementsRepository = ElementsRepository(app)
        val updateRepository = UpdateRepository(app)
        ExploreViewModel(
            userRepository = userRepository,
            elementsRepository = elementsRepository,
            updateRepository = updateRepository
        )
    }
    val searchCriteriaState = viewModel.searchCriteriaState
    val nearbyState by viewModel.exploreState.collectAsState()
    val locationStatus by LocationFlow.collectAsState()

    LaunchedEffect(locationStatus) {
        val location = (locationStatus as? LocationStatus.Success)?.location
        if (location != null) {
            searchCriteriaState.location = location
            when (searchCriteriaState.searchType) {
                SearchType.Culture -> viewModel.postEvent(ExploreEvent.GetCultures)
                SearchType.Element -> viewModel.postEvent(ExploreEvent.GetElements)
                SearchType.Contribution -> viewModel.postEvent(ExploreEvent.GetContributions)
            }
        }
    }

    LaunchedEffect(nearbyState) {
        when (nearbyState) {
            is ExploreState.Navigate -> {
                val id = (nearbyState as ExploreState.Navigate).id
                val isContribution = (nearbyState as ExploreState.Navigate).isContribution
                val route = Route.Details.route + "?" + "${Route.Details.id}=$id" + "&" +
                        "${Route.Details.isContribution}=$isContribution"
                navigate(route)
                viewModel.postEvent(ExploreEvent.Idle)
            }

            is ExploreState.Idle -> {
                viewModel.postEvent(ExploreEvent.GetUser)
            }

            else -> {}
        }
    }

    LaunchedEffect(
        searchCriteriaState.location,
        searchCriteriaState.searchString,
        searchCriteriaState.page,
        searchCriteriaState.radius,
        searchCriteriaState.types,
        searchCriteriaState.searchType
    ) {
        when (searchCriteriaState.searchType) {
            SearchType.Culture -> viewModel.postEvent(ExploreEvent.GetCultures)
            SearchType.Element -> viewModel.postEvent(ExploreEvent.GetElements)
            SearchType.Contribution -> viewModel.postEvent(ExploreEvent.GetContributions)
        }
    }

    RegisterLocationChanges()

    Explore(navigate, searchCriteriaState, nearbyState, viewModel::postEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Explore(
    navigate: (String) -> Unit,
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
            onTosClicked = { navigate(Route.WebView.ToS.route) },
            onPrivacyClicked = { navigate(Route.WebView.Privacy.route) }
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
        topBar = {
            SearchAppBar(
                searchString = searchCriteriaState.searchString ?: "",
                onFiltersClicked = { showFiltersDialog = true },
                onSearchClicked = { searchCriteriaState.searchString = it })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navigate(Route.AddNew.Location.route) }) {
                Icon(AppIcon.Add.getPainter(), contentDescription = "Add new element")
            }
        }
    ) { padding ->
        ShowBanners(padding)

        when (exploreState) {
            is ExploreState.Navigate,
            ExploreState.GetContributions,
            ExploreState.GetCultures,
            ExploreState.GetElements,
            ExploreState.Idle,
            ExploreState.Success.UserFetched,
            ExploreState.Loading -> LoadingComposable(padding)

            is ExploreState.Error -> {
                Text(
                    modifier = Modifier.fillMaxSize(),
                    text = stringResource(R.string.generic_sorry)
                )
            }

            is ExploreState.Success.ContributionsReceived,
            is ExploreState.Success.CulturesReceived,
            is ExploreState.Success.ElementsReceived -> {
                ExploreBody(
                    Modifier.padding(padding),
                    exploreState,
                    searchCriteriaState,
                    postEvent,
                )
            }
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