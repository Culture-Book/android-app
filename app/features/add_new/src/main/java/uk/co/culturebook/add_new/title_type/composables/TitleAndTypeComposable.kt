package uk.co.culturebook.add_new.title_type.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import uk.co.culturebook.add_new.title_type.events.TitleAndTypeEvent
import uk.co.culturebook.add_new.title_type.states.TitleAndTypeState
import uk.co.culturebook.data.models.cultural.ElementType
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppIcon
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.molecules.LoadingComposable
import uk.co.culturebook.ui.theme.molecules.TitleAndSubtitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleAndTypeScreen(
    state: TitleAndTypeState,
    onBack: () -> Unit,
    postEvent: (TitleAndTypeEvent) -> Unit
) {
    val selectedElementType = remember { mutableStateOf<ElementType?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    var title by remember { mutableStateOf("") }
    val isError by remember { derivedStateOf { !title.matches(Regex("[a-zA-Z ]+")) } }
    val type = selectedElementType.value
    val error = if (state is TitleAndTypeState.Error) stringResource(state.stringId) else null

    LaunchedEffect(error) {
        if (error != null) {
            snackbarHostState.showSnackbar(error)
            postEvent(TitleAndTypeEvent.Idle)
        }
    }

    Scaffold(
        topBar = { TitleAndTypeAppbar(onBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        when (state) {
            is TitleAndTypeState.Duplicate -> {
                val string = stringResource(R.string.duplicate_element_title)
                LaunchedEffect(state) {
                    snackbarHostState.showSnackbar(string)
                    postEvent(TitleAndTypeEvent.Idle)
                }
            }
            TitleAndTypeState.Loading -> LoadingComposable(padding)
            TitleAndTypeState.Idle,
            is TitleAndTypeState.Error,
            is TitleAndTypeState.Success -> {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(horizontal = mediumSize)
                ) {

                    TitleAndSubtitle(
                        title = stringResource(R.string.select_type_title),
                        message = stringResource(R.string.select_type_message)
                    )

                    ElementTypesComposable(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = mediumSize),
                        selectedType = selectedElementType.value,
                        onTypeClicked = { selectedElementType.value = it })

                    TitleAndSubtitle(
                        title = stringResource(R.string.select_type_title),
                        message = stringResource(R.string.select_type_message)
                    )

                    TitleComposable(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = mediumSize),
                        title = title,
                        onTitleChange = { title = it },
                        isError = isError
                    )

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isError && type != null,
                        onClick = {
                            if (type != null) {
                                postEvent(TitleAndTypeEvent.Submit(title, type))
                            }
                        }) {
                        Text(stringResource(R.string.next))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleAndTypeAppbar(navigateBack: () -> Unit = {}) {
    TopAppBar(modifier = Modifier.fillMaxWidth(),
        title = { Text(stringResource(R.string.select_type_and_title_title)) },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    painter = AppIcon.ArrowBack.getPainter(), contentDescription = "navigate back"
                )
            }
        })
}