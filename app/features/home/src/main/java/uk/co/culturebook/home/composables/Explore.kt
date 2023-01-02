package uk.co.culturebook.home.composables

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import uk.co.culturebook.data.flows.EventBus
import uk.co.culturebook.data.repositories.authentication.UserRepository
import uk.co.culturebook.home.events.ExploreEvent
import uk.co.culturebook.home.states.ExploreState
import uk.co.culturebook.home.viewModels.ExploreViewModel
import uk.co.culturebook.nav.Route
import uk.co.culturebook.ui.R

@Composable
fun ExploreRoute(navController: NavController) {
    val viewModel = viewModel {
        val userRepository =
            UserRepository((this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application))
        ExploreViewModel(userRepository = userRepository)
    }

    val nearbyState by viewModel.exploreState.collectAsState()
    Explore(navController, nearbyState, viewModel::postEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Explore(
    navController: NavController,
    exploreState: ExploreState,
    postEvent: (ExploreEvent) -> Unit
) {
    Scaffold(
        bottomBar = {
            AppBottomAppBar(
                currentDestination = navController.currentDestination,
                onItemClicked = { navController.navigate(it.route) })
        }
    ) { paddingValues ->
        when (exploreState) {
            is ExploreState.Error.Generic, ExploreState.Error.ToSUpdate ->
                ToSDialog(
                    modifier = Modifier.padding(paddingValues),
                    onCancel = { EventBus.logout() },
                    onAccept = { postEvent(ExploreEvent.UpdateToS) },
                    onTosClicked = { navController.navigate(Route.WebView.ToS.route) },
                    onPrivacyClicked = { navController.navigate(Route.WebView.Privacy.route) }
                )
            ExploreState.Idle, is ExploreState.Error -> LaunchedEffect(Unit) {
                postEvent(ExploreEvent.GetUser)
            }
            ExploreState.Loading -> Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
            ExploreState.Success -> Text("EXPLORE Success")
        }
    }
}

@Composable
fun ToSDialog(
    modifier: Modifier,
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