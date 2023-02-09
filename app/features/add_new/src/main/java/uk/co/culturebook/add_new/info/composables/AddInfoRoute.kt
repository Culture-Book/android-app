package uk.co.culturebook.add_new.info.composables

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
import androidx.navigation.NavController
import uk.co.culturebook.add_new.AddInfoViewModel
import uk.co.culturebook.add_new.data.InfoData
import uk.co.culturebook.add_new.info.events.AddInfoEvent
import uk.co.culturebook.add_new.info.states.AddInfoState
import uk.co.culturebook.data.models.cultural.ElementType
import uk.co.culturebook.data.models.cultural.MediaFile
import uk.co.culturebook.data.utils.getFileName
import uk.co.culturebook.data.utils.getFileSize
import uk.co.culturebook.data.utils.getMimeType
import uk.co.culturebook.nav.Route
import uk.co.culturebook.nav.toJsonString
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppIcon
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.molecules.LoadingComposable
import uk.co.culturebook.ui.utils.ShowSnackbar

@Composable
fun AddInfoRoute(
    navController: NavController,
    type: ElementType,
    onDone: (InfoData) -> Unit
) {
    val viewModel = viewModel { AddInfoViewModel() }
    val state by viewModel.addInfoState.collectAsState()
    val infoData = viewModel.infoData.apply {
        elementType = type.name
    }

    AddInfoScreen(
        state,
        onBack = { navController.navigateUp() },
        postEvent = viewModel::postEvent,
        infoData = infoData,
        navigate = { navController.navigate(it) },
        onDone = onDone
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddInfoScreen(
    state: AddInfoState,
    onBack: () -> Unit,
    infoData: InfoData,
    postEvent: (AddInfoEvent) -> Unit,
    navigate: (String) -> Unit,
    onDone: (InfoData) -> Unit
) {
    val snackbarState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()

    val addFilesLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents()) {
            val mediaFiles = it.mapNotNull { uri ->
                val fileName = context.getFileName(uri) ?: "file"
                val inputStream = context.contentResolver.openInputStream(uri)
                val contentType = context.getMimeType(uri) ?: ""
                val fileSize = context.getFileSize(uri)

                inputStream?.let {
                    MediaFile(fileName, uri, inputStream, fileSize, contentType)
                }
            }
            postEvent(AddInfoEvent.AddFile(mediaFiles))
        }

    LaunchedEffect(state) {
        when (state) {
            is AddInfoState.NavigateNext -> {
                onDone(infoData)
                navigate(Route.AddNew.Review.route)
            }
            is AddInfoState.AddLinkElements -> {
                val linkedElements =
                    state.infoData.linkedElements.map { it.toString() }.toJsonString()
                navigate("${Route.AddNew.AddInfo.LinkElements.route}$linkedElements")
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
                infoData = infoData,
                onLinkElementsClicked = { postEvent(AddInfoEvent.LinkElements(infoData.linkedElements)) },
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