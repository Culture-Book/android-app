package uk.co.culturebook.account.elements

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import uk.co.common.*
import uk.co.culturebook.account.SimpleBackAppBar
import uk.co.culturebook.data.models.cultural.SearchType
import uk.co.culturebook.data.repositories.cultural.ElementsRepository
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.molecules.LoadingComposable
import uk.co.culturebook.ui.utils.ShowSnackbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElementsRoute(navController: NavController) {
    val viewModel = viewModel {
        val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
        ElementsViewModel(ElementsRepository(app))
    }
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(
        viewModel.searchCriteriaState.searchType,
        viewModel.searchCriteriaState.page,
        viewModel.searchCriteriaState.getFavourites
    ) {
        when (viewModel.searchCriteriaState.searchType) {
            SearchType.Element -> viewModel.postEvent(ElementsEvent.FetchElements)
            SearchType.Contribution -> viewModel.postEvent(ElementsEvent.FetchContributions)
            SearchType.Culture -> viewModel.postEvent(ElementsEvent.FetchCultures)
        }
    }

    if (state is ElementsState.Error) {
        ShowSnackbar(
            snackbarState = snackbarHostState,
            stringId = (state as ElementsState.Error).messageId,
            onShow = { viewModel.postEvent(ElementsEvent.Idle) }
        )
    }

    when (state) {
        is ElementsState.DeleteContribution -> {
            AlertDialog(
                onDismissRequest = { viewModel.postEvent(ElementsEvent.Idle) },
                title = { Text(text = stringResource(id = R.string.delete_element)) },
                text = { Text(text = stringResource(id = R.string.delete_element_text)) },
                confirmButton = {
                    FilledTonalButton(onClick = {
                        viewModel.postEvent(ElementsEvent.ConfirmDeleteContribution((state as ElementsState.DeleteContribution).uuid))
                    }) {
                        Text(text = stringResource(id = R.string.delete))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.postEvent(ElementsEvent.Idle) }) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                }
            )
        }
        is ElementsState.DeleteCulture -> {
            AlertDialog(
                onDismissRequest = { viewModel.postEvent(ElementsEvent.Idle) },
                title = { Text(text = stringResource(id = R.string.delete_element)) },
                text = { Text(text = stringResource(id = R.string.delete_element_text)) },
                confirmButton = {
                    FilledTonalButton(onClick = {
                        viewModel.postEvent(ElementsEvent.ConfirmDeleteCulture((state as ElementsState.DeleteCulture).uuid))
                    }) {
                        Text(text = stringResource(id = R.string.delete))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.postEvent(ElementsEvent.Idle) }) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                }
            )
        }
        is ElementsState.DeleteElement -> {
            AlertDialog(
                onDismissRequest = { viewModel.postEvent(ElementsEvent.Idle) },
                title = { Text(text = stringResource(id = R.string.delete_element)) },
                text = { Text(text = stringResource(id = R.string.delete_element_text)) },
                confirmButton = {
                    FilledTonalButton(onClick = {
                        viewModel.postEvent(ElementsEvent.ConfirmDeleteElement((state as ElementsState.DeleteElement).uuid))
                    }) {
                        Text(text = stringResource(id = R.string.delete))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.postEvent(ElementsEvent.Idle) }) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                }
            )
        }
        else -> {}
    }

    Scaffold(
        topBar = {
            SimpleBackAppBar(
                title = stringResource(id = R.string.elements),
                onBackTapped = { navController.navigateUp() })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .padding(mediumSize)) {
            TypeFilters(
                type = viewModel.searchCriteriaState.searchType,
                onTypeChanged = {
                    viewModel.searchCriteriaState.searchType = it
                },
                showFavourites = viewModel.searchCriteriaState.getFavourites,
                onShowFavourites = { viewModel.searchCriteriaState.getFavourites = it },
            )
            when (state) {
                is ElementsState.Loading -> LoadingComposable(padding)
                is ElementsState.ContributionsFetched ->
                    ShowContributions(
                        contributions = (state as ElementsState.ContributionsFetched).contributions,
                        onClicked = { navController.navigate("") },
                        onEditClicked = { viewModel.postEvent(ElementsEvent.DeleteContribution(it.id!!)) }
                    ) {
                        PageInformation(
                            modifier = Modifier.fillMaxWidth(),
                            items = (state as ElementsState.ContributionsFetched).contributions,
                            onNextPage = { viewModel.searchCriteriaState.page++ },
                            onPreviousPage = { viewModel.searchCriteriaState.page-- },
                        )
                    }
                is ElementsState.CulturesFetched -> {
                    ShowCultures(
                        cultures = (state as ElementsState.CulturesFetched).cultures,
                        onClicked = { navController.navigate("") },
                        onEditClicked = { viewModel.postEvent(ElementsEvent.DeleteCulture(it.id!!)) }
                    ) {
                        PageInformation(
                            modifier = Modifier.fillMaxWidth(),
                            items = (state as ElementsState.CulturesFetched).cultures,
                            onNextPage = { viewModel.searchCriteriaState.page++ },
                            onPreviousPage = { viewModel.searchCriteriaState.page-- },
                        )
                    }
                }
                is ElementsState.ElementsFetched -> {
                    ShowElements(
                        elements = (state as ElementsState.ElementsFetched).elements,
                        onElementClicked = { navController.navigate("") },
                        onEditClicked = { viewModel.postEvent(ElementsEvent.DeleteElement(it.id!!)) }
                    ) {
                        PageInformation(
                            modifier = Modifier.fillMaxWidth(),
                            items = (state as ElementsState.ElementsFetched).elements,
                            onNextPage = { viewModel.searchCriteriaState.page++ },
                            onPreviousPage = { viewModel.searchCriteriaState.page-- },
                        )
                    }
                }
                is ElementsState.FavouriteContributionsFetched ->
                    ShowContributions(
                        contributions = (state as ElementsState.FavouriteContributionsFetched).contributions,
                        onClicked = { navController.navigate("") },
                    ) {
                        PageInformation(
                            modifier = Modifier.fillMaxWidth(),
                            items = (state as ElementsState.FavouriteContributionsFetched).contributions,
                            onNextPage = { viewModel.searchCriteriaState.page++ },
                            onPreviousPage = { viewModel.searchCriteriaState.page-- },
                        )
                    }
                is ElementsState.FavouriteCulturesFetched -> {
                    ShowCultures(
                        cultures = (state as ElementsState.FavouriteCulturesFetched).cultures,
                        onClicked = { navController.navigate("") },
                    ) {
                        PageInformation(
                            modifier = Modifier.fillMaxWidth(),
                            items = (state as ElementsState.FavouriteCulturesFetched).cultures,
                            onNextPage = { viewModel.searchCriteriaState.page++ },
                            onPreviousPage = { viewModel.searchCriteriaState.page-- },
                        )
                    }
                }
                is ElementsState.FavouriteElementsFetched -> {
                    ShowElements(
                        elements = (state as ElementsState.FavouriteElementsFetched).elements,
                        onElementClicked = { navController.navigate("") },
                    ) {
                        PageInformation(
                            modifier = Modifier.fillMaxWidth(),
                            items = (state as ElementsState.FavouriteElementsFetched).elements,
                            onNextPage = { viewModel.searchCriteriaState.page++ },
                            onPreviousPage = { viewModel.searchCriteriaState.page-- },
                        )
                    }
                }
                else -> {}
            }
        }

    }
}