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
import uk.co.culturebook.data.repositories.cultural.NearbyRepository
import uk.co.culturebook.data.repositories.cultural.UpdateRepository
import uk.co.culturebook.data.utils.toUUID
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
        val nearbyRepository = NearbyRepository(app)
        DetailsViewModel(nearbyRepository, updateRepository)
    }
    val snackbarState = remember { SnackbarHostState() }
    val state by viewModel.detailStateFlow.collectAsState()
    val uuid = remember {
        navController.currentBackStackEntry?.arguments?.getString(id).toUUID()
    }
    val isContribution = remember {
        navController.currentBackStackEntry?.arguments?.getBoolean(isContribution, false) ?: false
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
            else -> {}
        }
    }

    if (state is DetailState.Error)
        ShowSnackbar(stringId = (state as DetailState.Error).error, snackbarState = snackbarState)

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarState) },
        topBar = {
            when (state) {
                is DetailState.ElementReceived -> {
                    DetailsAppbar(
                        navigateBack = { navController.navigateUp() },
                        showActions = true,
                        isFavourite = (state as? DetailState.ElementReceived)?.element?.favourite
                            ?: false,
                        onFavouriteClicked = {
                            (state as? DetailState.ElementReceived)?.element?.let {
                                viewModel.postEvent(DetailEvent.FavouriteElement(it))
                            }
                        },
                        onBlock = {
                            (state as? DetailState.ElementReceived)?.element?.let {
                                viewModel.postEvent(DetailEvent.BlockElement(it.id))
                            }
                        },
                        onHide = {
                            (state as? DetailState.ElementReceived)?.element?.let {
                                viewModel.postEvent(DetailEvent.BlockElement(it.id))
                            }
                        },
                        onReport = {
                            (state as? DetailState.ElementReceived)?.element?.let {
                                viewModel.postEvent(DetailEvent.BlockElement(it.id))
                            }
                        }
                    )
                }
                else -> DetailsAppbar(
                    navigateBack = { navController.navigateUp() },
                    showActions = false
                )
            }

        },
    ) { padding ->
        when (state) {
            is DetailState.Loading -> LoadingComposable(padding)
            is DetailState.ElementReceived -> {
                val element = (state as DetailState.ElementReceived).element
                ElementDetailScreen(
                    modifier = Modifier.padding(padding),
                    element = element,
                    onContributionsClicked = { viewModel.postEvent(DetailEvent.GetContributions(it)) },
                    onCommentSent = {
                        viewModel.postEvent(DetailEvent.AddComment(element.id!!, it))
                    },
                    onCommentBlocked = {
                        viewModel.postEvent(DetailEvent.BlockComment(it))
                    },
                    onAddReaction = { viewModel.postEvent(DetailEvent.AddReaction(it)) }
                )
            }
            is DetailState.ContributionReceived -> {
                val contribution = (state as DetailState.ContributionReceived).contribution
                ContributionDetailScreen(
                    modifier = Modifier.padding(padding),
                    contribution = contribution,
                    onCommentSent = {
                        viewModel.postEvent(DetailEvent.AddComment(contribution.id!!, it))
                    },
                    onCommentBlocked = {
                        viewModel.postEvent(DetailEvent.BlockComment(it))
                    },
                    onAddReaction = { viewModel.postEvent(DetailEvent.AddReaction(it)) }
                )
            }
            else -> {}
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
                    painter = AppIcon.Close.getPainter(),
                    contentDescription = "navigate back"
                )
            }
        })
}