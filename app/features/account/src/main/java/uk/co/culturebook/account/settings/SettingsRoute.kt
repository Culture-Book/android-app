package uk.co.culturebook.account.settings

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
import uk.co.culturebook.account.SimpleBackAppBar
import uk.co.culturebook.data.models.cultural.BlockedList
import uk.co.culturebook.data.models.cultural.SearchType
import uk.co.culturebook.data.repositories.authentication.UserRepository
import uk.co.culturebook.data.repositories.cultural.UpdateRepository
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.utils.ShowSnackbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsRoute(navController: NavController) {
    val viewModel = viewModel {
        val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
        SettingsViewModel(UserRepository(app), UpdateRepository(app))
    }
    val snackbarState = remember { SnackbarHostState() }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showBlockedElements by remember { mutableStateOf(false) }
    var blockedList by remember { mutableStateOf(BlockedList()) }
    val state by viewModel.state.collectAsState()

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(text = stringResource(id = R.string.delete_account)) },
            text = { Text(text = stringResource(id = R.string.delete_account_confirmation)) },
            confirmButton = {
                FilledTonalButton(onClick = {
                    showDeleteDialog = false
                    viewModel.postEvent(SettingsEvent.DeleteAccount)
                }) {
                    Text(text = stringResource(id = R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        )
    }

    if (showBlockedElements) {
        BlockedElementsSheet(
            onDismiss = { showBlockedElements = false },
            onUnblock = { uuid, type ->
                when (type) {
                    SearchType.Element -> viewModel.postEvent(SettingsEvent.UnblockElement(uuid))
                    SearchType.Contribution -> viewModel.postEvent(
                        SettingsEvent.UnblockContribution(
                            uuid
                        )
                    )
                    SearchType.Culture -> viewModel.postEvent(SettingsEvent.UnblockCulture(uuid))
                }
            }
        )
    }

    LaunchedEffect(state) {
        when (state) {
            is SettingsState.AccountDeleted -> navController.navigateUp()
            is SettingsState.ElementUnblocked -> showBlockedElements = true
            is SettingsState.BlockedListFetched -> blockedList =
                (state as SettingsState.BlockedListFetched).blockedList
            else -> {}
        }
    }

    if (state is SettingsState.Error) {
        ShowSnackbar(
            stringId = (state as SettingsState.Error).messageId,
            snackbarState = snackbarState
        )
        viewModel.postEvent(SettingsEvent.Idle)
    }

    Scaffold(
        topBar = {
            SimpleBackAppBar(
                title = stringResource(id = R.string.settings),
                onBackTapped = { navController.navigateUp() })
        },
        snackbarHost = { SnackbarHost(snackbarState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = mediumSize)
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    showBlockedElements = true
                    viewModel.postEvent(SettingsEvent.FetchBlockedList)
                }) {
                Text(text = stringResource(id = R.string.blocked_elements))
            }

            FilledTonalButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = mediumSize),
                onClick = { showDeleteDialog = true }) {
                Text(text = stringResource(id = R.string.delete_account))
            }
        }
    }
}