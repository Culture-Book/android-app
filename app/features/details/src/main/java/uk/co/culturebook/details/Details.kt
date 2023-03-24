package uk.co.culturebook.details

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import uk.co.common.*
import uk.co.culturebook.data.models.cultural.*
import uk.co.culturebook.data.repositories.cultural.DetailsRepository
import uk.co.culturebook.data.repositories.cultural.ElementsRepository
import uk.co.culturebook.data.repositories.cultural.UpdateRepository
import uk.co.culturebook.data.utils.toUUID
import uk.co.culturebook.nav.Route
import uk.co.culturebook.nav.Route.Details.id
import uk.co.culturebook.nav.Route.Details.isContribution
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.*
import uk.co.culturebook.ui.theme.molecules.*
import uk.co.culturebook.ui.utils.ShowSnackbar
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreenRoute(navController: NavController) {
    val viewModel = viewModel {
        val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
        val updateRepository = UpdateRepository(app)
        val elementsRepository = ElementsRepository(app)
        val detailsRepository = DetailsRepository(app)
        DetailsViewModel(elementsRepository, updateRepository, detailsRepository)
    }
    val snackbarState = remember { SnackbarHostState() }
    val state by viewModel.detailStateFlow.collectAsState()
    val uuid = remember {
        navController.currentBackStackEntry?.arguments?.getString(id).toUUID()
    }
    val isContribution = remember {
        navController.currentBackStackEntry?.arguments?.getBoolean(isContribution, false) ?: false
    }
    var element by remember {
        mutableStateOf<Element?>(null)
    }
    var contribution by remember {
        mutableStateOf<Contribution?>(null)
    }
    var comments by remember {
        mutableStateOf(emptyList<Comment>())
    }

    LaunchedEffect(uuid) {
        if (isContribution) {
            viewModel.postEvent(DetailEvent.GetContribution(uuid))
        } else {
            viewModel.postEvent(DetailEvent.GetElement(uuid))
        }
    }

    LaunchedEffect(state) {
        when (state) {
            is DetailState.Blocked -> {
                navController.navigateUp()
            }
            is DetailState.ElementCommentsReceived -> {
                comments = (state as DetailState.ElementCommentsReceived).comments
            }
            is DetailState.ContributionCommentsReceived -> {
                comments = (state as DetailState.ContributionCommentsReceived).comments
            }
            is DetailState.ElementReceived -> {
                element = (state as DetailState.ElementReceived).element
            }
            is DetailState.ContributionReceived -> {
                contribution = (state as DetailState.ContributionReceived).contribution
            }
            else -> {}
        }
    }

    if (state is DetailState.Error)
        ShowSnackbar(stringId = (state as DetailState.Error).error, snackbarState = snackbarState)

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarState) },
        topBar = {
            if (element != null) {
                DetailsAppbar(
                    navigateBack = { navController.navigateUp() },
                    showActions = true,
                    isFavourite = element?.favourite ?: false,
                    onFavouriteClicked = {
                        element?.let { viewModel.postEvent(DetailEvent.FavouriteElement(it)) }
                    },
                    onBlock = {
                        element?.let { viewModel.postEvent(DetailEvent.BlockElement(it.id)) }
                    },
                    onHide = {
                        element?.let { viewModel.postEvent(DetailEvent.BlockElement(it.id)) }
                    },
                    onReport = {
                        element?.let { viewModel.postEvent(DetailEvent.BlockElement(it.id)) }
                    }
                )
            } else if (contribution != null) {
                DetailsAppbar(
                    navigateBack = { navController.navigateUp() },
                    showActions = true,
                    isFavourite = contribution?.favourite ?: false,
                    onFavouriteClicked = {
                        contribution?.let { viewModel.postEvent(DetailEvent.FavouriteContribution(it.id!!)) }
                    },
                    onBlock = {
                        contribution?.let { viewModel.postEvent(DetailEvent.BlockContribution(it.id)) }
                    },
                    onHide = {
                        contribution?.let { viewModel.postEvent(DetailEvent.BlockContribution(it.id)) }
                    },
                    onReport = {
                        contribution?.let { viewModel.postEvent(DetailEvent.BlockContribution(it.id)) }
                    }
                )
            } else {
                DetailsAppbar(
                    navigateBack = { navController.navigateUp() },
                    showActions = false
                )
            }
        },
    ) { padding ->
        when (state) {
            is DetailState.Loading -> LoadingComposable(padding)
            else -> {
                if (element != null) {
                    element?.let { element ->
                        ElementDetailScreen(
                            modifier = Modifier.padding(padding),
                            comments = comments,
                            element = element,
                            onContributionsClicked = {
                                val route =
                                    Route.Details.ShowContributions.route + "?" + "${Route.Details.ShowContributions.elementId}=$it"
                                navController.navigate(route)

                            },
                            onCommentSent = {
                                viewModel.postEvent(DetailEvent.AddElementComment(element.id!!, it))
                            },
                            onCommentBlocked = {
                                viewModel.postEvent(
                                    DetailEvent.BlockElementComment(
                                        element.id!!,
                                        it
                                    )
                                )
                            },
                            onAddReaction = {
                                viewModel.postEvent(
                                    DetailEvent.ToggleElementReaction(
                                        element.id!!,
                                        it
                                    )
                                )
                            },
                            onDeleteComment = {
                                viewModel.postEvent(
                                    DetailEvent.DeleteElementComment(
                                        element.id!!,
                                        it
                                    )
                                )
                            },
                            onDeleteReaction = {
                                viewModel.postEvent(
                                    DetailEvent.ToggleElementReaction(
                                        element.id!!,
                                        it
                                    )
                                )
                            },
                            onGetComments = {
                                viewModel.postEvent(
                                    DetailEvent.GetElementComments(
                                        element.id!!
                                    ),
                                    false
                                )
                            },
                        )
                    }
                } else if (contribution != null) {
                    contribution?.let { contribution ->
                        ContributionDetailScreen(
                            modifier = Modifier.padding(padding),
                            contribution = contribution,
                            comments = comments,
                            onCommentSent = {
                                viewModel.postEvent(
                                    DetailEvent.AddContributionComment(
                                        contribution.id!!,
                                        it
                                    )
                                )
                            },
                            onCommentBlocked = {
                                viewModel.postEvent(
                                    DetailEvent.BlockContributionComment(
                                        contribution.id!!,
                                        it
                                    )
                                )
                            },
                            onAddReaction = {
                                viewModel.postEvent(
                                    DetailEvent.ToggleContributionReaction(
                                        contribution.id!!,
                                        it
                                    )
                                )
                            },
                            onDeleteComment = {
                                viewModel.postEvent(
                                    DetailEvent.DeleteContributionComment(
                                        contribution.id!!,
                                        it
                                    )
                                )
                            },
                            onDeleteReaction = {
                                viewModel.postEvent(
                                    DetailEvent.ToggleContributionReaction(
                                        contribution.id!!,
                                        it
                                    )
                                )
                            },
                            onGetComments = {
                                viewModel.postEvent(
                                    DetailEvent.GetContributionComments(
                                        contribution.id!!
                                    ),
                                    false
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsAppbar(
    title: String = stringResource(R.string.details),
    showActions: Boolean = false,
    navigateBack: () -> Unit = {},
    onFavouriteClicked: () -> Unit = {},
    isFavourite: Boolean = false,
    onHide: () -> Unit = {},
    onBlock: () -> Unit = {},
    onReport: () -> Unit = {}
) {
    TopAppBar(modifier = Modifier.fillMaxWidth(),
        actions = {
            if (showActions) {
                var expanded by remember { mutableStateOf(false) }
                IconButton(onClick = { onFavouriteClicked() }) {
                    if (isFavourite) {
                        Icon(AppIcon.FavouriteFilled.getPainter(), contentDescription = "fav")
                    } else {
                        Icon(AppIcon.FavouriteOutline.getPainter(), contentDescription = "fav")
                    }
                }
                Box(
                    modifier = Modifier
                        .wrapContentSize(Alignment.TopStart)
                ) {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "options")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                                onHide()
                            },
                            text = { Text(stringResource(R.string.hide)) }
                        )
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                                onReport()
                            },
                            text = { Text(stringResource(R.string.report)) }
                        )
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                                onBlock()
                            },
                            text = { Text(stringResource(R.string.block)) }
                        )
                    }
                }
            }
        },
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    painter = AppIcon.ArrowBack.getPainter(),
                    contentDescription = "navigate back"
                )
            }
        })
}