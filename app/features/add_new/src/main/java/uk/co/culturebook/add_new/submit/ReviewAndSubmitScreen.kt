package uk.co.culturebook.add_new.submit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import uk.co.culturebook.add_new.data.AddNewState
import uk.co.culturebook.add_new.location.composables.choose_location.LocationBody
import uk.co.culturebook.add_new.title_type.composables.icon
import uk.co.culturebook.add_new.title_type.composables.label
import uk.co.culturebook.data.models.cultural.ElementType
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.*
import uk.co.culturebook.ui.theme.molecules.*
import uk.co.culturebook.ui.utils.ShowSnackbar
import uk.co.culturebook.ui.utils.prettyPrint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitScreenComposable(
    onBack: () -> Unit,
    addNewState: AddNewState,
    state: SubmitState,
    onSubmit: (AddNewState) -> Unit
) {
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    if (state is SubmitState.Error) ShowSnackbar(
        stringId = R.string.generic_sorry,
        snackbarState = snackbarHostState
    )

    when (state) {
        SubmitState.Loading -> LoadingComposable()
        SubmitState.Error,
        SubmitState.Idle,
        SubmitState.Success ->
            Scaffold(
                topBar = { ReviewAppbar(onBack) },
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(horizontal = mediumSize)
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                    horizontalAlignment = CenterHorizontally
                ) {
                    addNewState.files.takeIf { it.isNotEmpty() }?.first()?.let {
                        if (it.contentType.contains("image")) {
                            ImageComposable(
                                modifier = Modifier
                                    .size(height = xxxxlSize, width = xxxxlSize * 1.5f),
                                uri = it.uri,
                            )
                        } else if (it.contentType.contains("video")) {
                            VideoComposable(
                                modifier = Modifier
                                    .size(height = xxxxlSize, width = xxxxlSize * 1.5f),
                                uri = it.uri,
                            )
                        } else if (it.contentType.contains("audio")) {
                            AudioComposable(
                                modifier = Modifier
                                    .size(height = xxxxlSize, width = xxxxlSize * 1.5f),
                                uri = it.uri,
                            )
                        }
                    }

                    TitleAndSubtitle(
                        modifier = Modifier
                            .padding(top = mediumSize)
                            .fillMaxWidth(),
                        title = addNewState.name,
                        leadingTitleContent = {
                            addNewState.type?.let {
                                Icon(
                                    painter = it.icon,
                                    contentDescription = it.label
                                )
                            }
                        }
                    )

                    LargeDynamicRoundedTextField(
                        modifier = Modifier
                            .defaultMinSize(minHeight = xxxxlSize)
                            .padding(vertical = mediumSize)
                            .fillMaxWidth(),
                        value = addNewState.information,
                        readOnly = true
                    )

                    if (addNewState.type == ElementType.Event) {
                        TitleAndSubtitle(
                            modifier = Modifier.padding(vertical = smallSize),
                            title = stringResource(R.string.event_info)
                        )

                        if (addNewState.eventType?.startDateTime != null) {
                            OutlinedSurface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = smallSize),
                                title = stringResource(
                                    id = R.string.event_date,
                                    addNewState.eventType?.startDateTime?.prettyPrint() ?: ""
                                )
                            )
                        }

                        if (addNewState.eventType?.location != null) {
                            LocationBody(
                                modifier = Modifier
                                    .padding(bottom = mediumSize)
                                    .clip(mediumRoundedShape)
                                    .height(xxxxlSize)
                                    .fillMaxWidth(),
                                isDisplayOnly = true,
                                locationToShow = addNewState.eventType?.location
                            )
                        }
                    }

                    if (addNewState.files.isNotEmpty()) {
                        TitleAndSubtitle(
                            modifier = Modifier.padding(bottom = mediumSize),
                            title = stringResource(R.string.media)
                        )

                        LazyRow {
                            items(addNewState.files) {
                                if (it.contentType.contains("image")) {
                                    ImageComposable(
                                        modifier = Modifier
                                            .size(height = xxxxlSize, width = xxxxlSize * 1.5f)
                                            .padding(end = mediumSize),
                                        uri = it.uri,
                                    )
                                } else if (it.contentType.contains("video")) {
                                    VideoComposable(
                                        modifier = Modifier
                                            .size(height = xxxxlSize, width = xxxxlSize * 1.5f)
                                            .padding(end = mediumSize),
                                        uri = it.uri,
                                    )
                                } else if (it.contentType.contains("audio")) {
                                    AudioComposable(
                                        modifier = Modifier
                                            .size(height = xxxxlSize, width = xxxxlSize * 1.5f)
                                            .padding(end = mediumSize),
                                        uri = it.uri,
                                    )
                                }
                            }
                        }
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = mediumSize),
                        onClick = { onSubmit(addNewState) }
                    ) {
                        val text =
                            if (addNewState.isContribution) {
                                stringResource(R.string.submit_contribution)
                            } else {
                                stringResource(R.string.submit_element)
                            }
                        Text(text)
                    }
                }
            }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewAppbar(navigateBack: () -> Unit = {}) {
    TopAppBar(modifier = Modifier.fillMaxWidth(),
        title = { Text(stringResource(R.string.review_submit)) },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    painter = AppIcon.ArrowBack.getPainter(), contentDescription = "navigate back"
                )
            }
        })
}