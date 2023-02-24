package uk.co.culturebook.add_new.title_type.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.text.isDigitsOnly
import uk.co.common.ElementTypesComposable
import uk.co.common.label
import uk.co.culturebook.add_new.data.TypeData
import uk.co.culturebook.add_new.title_type.events.TitleAndTypeEvent
import uk.co.culturebook.add_new.title_type.states.TitleAndTypeState
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
    _typeData: TypeData,
    postEvent: (TitleAndTypeEvent) -> Unit
) {
    val typeData by remember { mutableStateOf(_typeData) }
    val snackbarHostState = remember { SnackbarHostState() }
    val isError by remember { derivedStateOf { typeData.name.isDigitsOnly() } }

    HandleError(
        state = state,
        postEvent = postEvent,
        typeData = typeData,
        snackbarHostState = snackbarHostState
    )

    Scaffold(
        topBar = { TitleAndTypeAppbar(onBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        when (state) {
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
                            .padding(vertical = mediumSize),
                        selectedType = typeData.type,
                        onTypeClicked = { typeData.type = it })

                    TitleAndSubtitle(
                        title = stringResource(R.string.select_title),
                        message = stringResource(R.string.select_title_message)
                    )

                    TitleComposable(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = mediumSize),
                        title = typeData.name,
                        onTitleChange = { typeData.name = it },
                        isError = isError
                    )

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isError && typeData.type != null,
                        onClick = {
                            if (typeData.type != null) {
                                postEvent(TitleAndTypeEvent.Submit(typeData))
                            }
                        }) {
                        Text(stringResource(R.string.next))
                    }
                }
            }
        }
    }
}

@Composable
fun HandleError(
    state: TitleAndTypeState,
    postEvent: (TitleAndTypeEvent) -> Unit,
    typeData: TypeData,
    snackbarHostState: SnackbarHostState
) {
    val error = if (state is TitleAndTypeState.Error) stringResource(state.stringId) else null

    if (state is TitleAndTypeState.Error.Duplicate) {
        AlertDialog(
            icon = {
                Icon(painter = AppIcon.Info.getPainter(), contentDescription = "info")
            },
            title = {
                Text(
                    text = stringResource(
                        id = R.string.duplicate_element_title,
                        state.element.name
                    )
                )
            },
            text = {
                Text(
                    text = stringResource(
                        id = R.string.duplicate_element_message,
                        state.element.type.label
                    )
                )
            },
            onDismissRequest = {
                postEvent(TitleAndTypeEvent.Idle)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        typeData.parentElement = state.element.id
                        postEvent(TitleAndTypeEvent.SubmitContribution(typeData))
                    }) {
                    Text(stringResource(R.string.next))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        typeData.parentElement = null
                        postEvent(TitleAndTypeEvent.Idle)
                    }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    } else {
        LaunchedEffect(error) {
            if (error != null) {
                snackbarHostState.showSnackbar(error)
                postEvent(TitleAndTypeEvent.Idle)
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