package uk.co.culturebook.add_new.info.composables

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.co.culturebook.add_new.AddInfoViewModel
import uk.co.culturebook.add_new.data.AddNewState
import uk.co.culturebook.add_new.info.events.AddInfoEvent
import uk.co.culturebook.add_new.info.states.AddInfoState
import uk.co.culturebook.data.models.cultural.MediaFile
import uk.co.culturebook.data.utils.getFileName
import uk.co.culturebook.data.utils.getFileSize
import uk.co.culturebook.data.utils.getMimeType
import uk.co.culturebook.nav.Route
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppIcon
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.molecules.LoadingComposable
import uk.co.culturebook.ui.utils.ShowSnackbar

@Composable
fun AddInfoRoute(
    navigateBack: () -> Unit,
    navigate: (String) -> Unit,
    addNewState: AddNewState
) {
    val viewModel = viewModel { AddInfoViewModel(addNewState) }
    val state by viewModel.addInfoState.collectAsState()

    AddInfoScreen(
        state,
        onBack = navigateBack,
        postEvent = viewModel::postEvent,
        addNewState = addNewState,
        navigate = navigate
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddInfoScreen(
    state: AddInfoState,
    onBack: () -> Unit,
    addNewState: AddNewState,
    postEvent: (AddInfoEvent) -> Unit,
    navigate: (String) -> Unit
) {
    val snackbarState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()

    val addFilesLauncher =
        rememberLauncherForActivityResult(contract = object :
            ActivityResultContracts.GetMultipleContents() {
            override fun createIntent(context: Context, input: String): Intent {
                val intent = super.createIntent(context, input).apply {
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                return intent
            }
        }) {
            val mediaFiles = it.map { uri ->
                val fileName = context.getFileName(uri) ?: "file"
                val contentType = context.getMimeType(uri) ?: ""
                val fileSize = context.getFileSize(uri)
                MediaFile(fileName, uri, fileSize, contentType)
            }
            postEvent(AddInfoEvent.AddFile(mediaFiles))
        }

    LaunchedEffect(state) {
        when (state) {
            is AddInfoState.NavigateNext -> {
                navigate(Route.AddNew.Review.route)
            }

            is AddInfoState.AddLinkElements -> {
                navigate(Route.AddNew.AddInfo.LinkElements.route)
            }

            else -> {}
        }
        postEvent(AddInfoEvent.Idle)
    }

    if (state is AddInfoState.Error) {
        ShowSnackbar(
            stringId = state.stringId,
            snackbarState = snackbarState,
            coroutine = coroutine
        )
    }

    Scaffold(
        topBar = { AddInfoAppbar(onBack) },
        snackbarHost = { SnackbarHost(hostState = snackbarState) }
    ) { padding ->
        when (state) {
            is AddInfoState.NavigateNext,
            AddInfoState.Loading -> LoadingComposable(padding)

            else -> AddInfoBody(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = mediumSize),
                addNewState = addNewState,
                onLinkElementsClicked = { postEvent(AddInfoEvent.LinkElements(addNewState.linkElements)) },
                onDateAdded = { postEvent(AddInfoEvent.AddDate(it)) },
                onLocationAdded = { postEvent(AddInfoEvent.AddLocation(it)) },
                onAddFilesClicked = { addFilesLauncher.launch("*/*") },
                onDeleteFile = { postEvent(AddInfoEvent.DeleteMediaFile(it)) },
                onSubmitClicked = { postEvent(AddInfoEvent.Submit(it)) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddInfoAppbar(navigateBack: () -> Unit = {}) {
    TopAppBar(modifier = Modifier.fillMaxWidth(),
        title = { Text(stringResource(R.string.add_info)) },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    painter = AppIcon.ArrowBack.getPainter(), contentDescription = "navigate back"
                )
            }
        })
}