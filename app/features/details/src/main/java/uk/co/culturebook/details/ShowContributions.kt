package uk.co.culturebook.details

import android.app.Application
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import uk.co.common.BlockOptionsState
import uk.co.common.PageInformation
import uk.co.common.ShowContributions
import uk.co.culturebook.data.repositories.cultural.DetailsRepository
import uk.co.culturebook.data.repositories.cultural.ElementsRepository
import uk.co.culturebook.data.repositories.cultural.UpdateRepository
import uk.co.culturebook.data.utils.toUUID
import uk.co.culturebook.nav.Route
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppIcon
import uk.co.culturebook.ui.theme.molecules.LoadingComposable
import uk.co.culturebook.ui.utils.ShowSnackbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowContributionsRoute(navController: NavController) {
    val viewModel = viewModel {
        val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)

        DetailsViewModel(
            ElementsRepository(app),
            UpdateRepository(app),
            DetailsRepository(app),
        )
    }
    val elementId =
        navController.currentBackStackEntry?.arguments?.getString(Route.Details.ShowContributions.elementId)
            ?.toUUID()
    val state by viewModel.detailStateFlow.collectAsState()
    val snackbarState = remember { SnackbarHostState() }
    val searchCriteria = viewModel.searchCriteria

    LaunchedEffect(searchCriteria.elementId, searchCriteria.page) {
        viewModel.postEvent(DetailEvent.GetContributions(elementId))
    }

    Scaffold(
        topBar = { ShowContributionsAppBar(onBack = { navController.navigateUp() }) },
        snackbarHost = { SnackbarHost(hostState = snackbarState) }
    ) { padding ->
        when (state) {
            is DetailState.ContributionsReceived -> {
                val contributions = (state as DetailState.ContributionsReceived).contributions
                ShowContributions(
                    contributions = contributions,
                    onOptionsClicked = {
                        when (it) {
                            is BlockOptionsState.Block -> viewModel.postEvent(
                                DetailEvent.BlockFromShowContributions(
                                    it.id, elementId!!
                                )
                            )
                            is BlockOptionsState.Hide -> viewModel.postEvent(
                                DetailEvent.BlockFromShowContributions(
                                    it.id, elementId!!
                                )
                            )
                            is BlockOptionsState.Report -> viewModel.postEvent(
                                DetailEvent.BlockFromShowContributions(
                                    it.id, elementId!!
                                )
                            )
                        }
                    },
                    onFavouriteClicked = {
                        viewModel.postEvent(
                            DetailEvent.FavouriteFromShowContributions(
                                it,
                                elementId!!
                            )
                        )
                    },
                    onClicked = { contribution ->
                        val route =
                            Route.Details.route + "?" + "${Route.Details.id}=${contribution.id}" + "&" +
                                    "${Route.Details.isContribution}=true"
                        navController.navigate(route)
                    },
                    lastComposable = {
                        PageInformation(
                            modifier = Modifier.fillMaxWidth(),
                            onNextPage = { searchCriteria.page = it },
                            onPreviousPage = { searchCriteria.page = it },
                            currentPage = searchCriteria.page,
                            items = contributions
                        )
                    }
                )
            }
            is DetailState.Error -> ShowSnackbar(
                stringId = (state as DetailState.Error).error,
                snackbarState
            )
            else -> LoadingComposable(padding)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowContributionsAppBar(onBack: () -> Unit) {
    TopAppBar(
        title = { Text(stringResource(R.string.show_contributions)) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painter = AppIcon.ArrowBack.getPainter(),
                    contentDescription = "back"
                )
            }
        },
    )
}