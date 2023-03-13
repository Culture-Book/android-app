package uk.co.culturebook.add_new.link_elements

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import uk.co.common.ElementOptionsState
import uk.co.common.ElementTypesComposableHorizontal
import uk.co.common.PageInformation
import uk.co.common.ShowElements
import uk.co.culturebook.data.models.cultural.Element
import uk.co.culturebook.data.models.cultural.SearchCriteriaState
import uk.co.culturebook.data.repositories.cultural.NearbyRepository
import uk.co.culturebook.data.repositories.cultural.UpdateRepository
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppIcon
import uk.co.culturebook.ui.theme.mediumRoundedShape
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.molecules.LoadingComposable
import uk.co.culturebook.ui.utils.ShowSnackbar
import java.util.*

@Composable
fun LinkElementsRoute(
    navController: NavController,
    elements: List<Element>,
    onSubmit: (List<Element>) -> Unit
) {
    val viewModel = viewModel {
        val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
        LinkElementsViewModel(elements, NearbyRepository(app), UpdateRepository(app))
    }
    val searchCriteria = viewModel.searchCriteria
    val state by viewModel.linkState.collectAsState()
    val selectedElements = viewModel.selectedElements

    LaunchedEffect(state) {
        if (state is LinkState.ElementsLinked) {
            if (navController.navigateUp()) {
                onSubmit((state as LinkState.ElementsLinked).elements)
            }
        }
    }

    LaunchedEffect(
        searchCriteria.searchString,
        searchCriteria.page,
        searchCriteria.types
    ) {
        viewModel.postEvent(LinkEvent.FetchElements(searchCriteria))
    }

    LinkBody(
        searchCriteriaState = searchCriteria,
        state = state,
        selectedElements = selectedElements,
        onElementClicked = { viewModel.postEvent(LinkEvent.LinkElement(it)) },
        onOptionsClicked = {
            when (it) {
                is ElementOptionsState.Block -> viewModel.postEvent(LinkEvent.BlockElement(it.id))
                is ElementOptionsState.Hide -> viewModel.postEvent(LinkEvent.BlockElement(it.id))
                is ElementOptionsState.Report -> viewModel.postEvent(LinkEvent.BlockElement(it.id))
            }
        },
        onFavouriteClicked = { viewModel.postEvent(LinkEvent.FavouriteElement(it)) },
        onSubmit = { viewModel.postEvent(LinkEvent.LinkElements) },
        onBack = { navController.navigateUp() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkBody(
    searchCriteriaState: SearchCriteriaState,
    state: LinkState,
    selectedElements: List<Element>,
    onElementClicked: (Element) -> Unit,
    onOptionsClicked: (ElementOptionsState) -> Unit,
    onFavouriteClicked: (UUID) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
) {
    val snackbarState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarState) },
        topBar = {
            LinkElementsAppbar(onBack)
        }
    ) { padding ->
        when (state) {
            is LinkState.ElementsLinked -> onSubmit()
            LinkState.Error -> ShowSnackbar(
                stringId = R.string.generic_sorry,
                snackbarState = snackbarState
            )
            is LinkState.ElementsFetched -> {
                Column {
                    SearchElementAndTypes(
                        Modifier.padding(padding),
                        searchCriteriaState = searchCriteriaState,
                        onSearch = { searchCriteriaState.searchString = it }
                    )

                    ShowElements(
                        elements = state.elements,
                        onElementClicked = onElementClicked,
                        onOptionsClicked = onOptionsClicked,
                        onFavouriteClicked = onFavouriteClicked,
                        selectedElements = selectedElements
                    ) {
                        PageInformation(
                            modifier = Modifier
                                .fillMaxWidth(),
                            onNextPage = { searchCriteriaState.page = it },
                            onPreviousPage = { searchCriteriaState.page = it },
                            currentPage = searchCriteriaState.page,
                            items = state.elements
                        )

                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(mediumSize),
                            onClick = onSubmit
                        ) {
                            Text(text = stringResource(id = R.string.submit))
                        }
                    }
                }
            }
            LinkState.Idle -> {
                SearchElementAndTypes(
                    Modifier.padding(padding),
                    searchCriteriaState = searchCriteriaState,
                    onSearch = {
                        searchCriteriaState.searchString = it
                    }
                )
            }
            LinkState.Loading -> LoadingComposable()
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchElementAndTypes(
    modifier: Modifier = Modifier,
    searchCriteriaState: SearchCriteriaState = SearchCriteriaState(),
    onSearch: (String) -> Unit
) {
    var searchString by remember { mutableStateOf(searchCriteriaState.searchString ?: "") }

    Column(
        modifier = modifier
            .padding(horizontal = mediumSize)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = mediumSize),
            value = searchString,
            onValueChange = { searchString = it },
            shape = mediumRoundedShape,
            trailingIcon = {
                IconButton(onClick = { onSearch(searchString) }) {
                    Icon(painter = AppIcon.Search.getPainter(), contentDescription = "search")
                }
            },
            leadingIcon = {
                IconButton(onClick = { searchString = "" }) {
                    Icon(painter = AppIcon.Close.getPainter(), contentDescription = "clear")
                }
            }
        )
        ElementTypesComposableHorizontal(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = mediumSize),
            selectedTypes = searchCriteriaState.types,
            onTypeClicked = { searchCriteriaState.toggleTypesSelection(it) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkElementsAppbar(navigateBack: () -> Unit = {}) {
    TopAppBar(modifier = Modifier.fillMaxWidth(),
        title = { Text(stringResource(R.string.link_elements)) },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    painter = AppIcon.ArrowBack.getPainter(), contentDescription = "navigate back"
                )
            }
        })
}