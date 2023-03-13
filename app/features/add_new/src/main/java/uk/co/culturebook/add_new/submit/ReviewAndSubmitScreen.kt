package uk.co.culturebook.add_new.submit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import uk.co.common.*
import uk.co.common.choose_location.LocationBody
import uk.co.culturebook.add_new.data.AddNewState
import uk.co.culturebook.data.models.cultural.ElementType
import uk.co.culturebook.data.models.cultural.isNotEmpty
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.*
import uk.co.culturebook.ui.theme.molecules.*
import uk.co.culturebook.ui.utils.prettyPrint
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitScreenComposable(
    onBack: () -> Unit,
    addNewState: AddNewState,
    onSubmit: (AddNewState) -> Unit
) {
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = { ReviewAppbar(onBack) },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = mediumSize)
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = mediumSize),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${stringResource(R.string.title)}: ",
                    style = MaterialTheme.typography.titleLarge
                )
                Icon(
                    modifier = Modifier.padding(horizontal = smallSize),
                    painter = addNewState.type?.icon ?: AppIcon.BrokenImage.getPainter(),
                    contentDescription = "type icon"
                )
                Text(
                    text = addNewState.name,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            TitleAndSubtitle(
                modifier = Modifier
                    .fillMaxWidth(),
                title = stringResource(R.string.description)
            )

            LargeDynamicRoundedTextField(
                modifier = Modifier
                    .defaultMinSize(minHeight = xxxxlSize)
                    .padding(vertical = mediumSize)
                    .fillMaxWidth(),
                value = addNewState.information,
                readOnly = true
            )

            if (addNewState.linkElements.isNotEmpty()) {
                TitleAndSubtitle(
                    modifier = Modifier.padding(bottom = smallSize),
                    title = stringResource(R.string.linked_elements, addNewState.linkElements.size),
                    titleType = TitleType.Small
                )
            }

            if (addNewState.type == ElementType.Event) {
                TitleAndSubtitle(
                    modifier = Modifier.padding(vertical = smallSize),
                    title = stringResource(R.string.event_info)
                )

                if (addNewState.eventType != null) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        if (addNewState.eventType?.startDateTime != LocalDateTime.MIN) {
                            OutlinedSurface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(.5f)
                                    .padding(end = smallSize)
                                    .height(xxxxlSize),
                                icon = {
                                    Icon(
                                        painter = AppIcon.Calendar.getPainter(),
                                        contentDescription = "calendar"
                                    )
                                },
                                title = addNewState.eventType?.startDateTime?.prettyPrint() ?: ""
                            )
                        }

                        if (addNewState.eventType?.location.isNotEmpty()) {
                            LocationBody(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(.5f)
                                    .clip(mediumRoundedShape)
                                    .height(xxxxlSize),
                                isDisplayOnly = true,
                                locationToShow = addNewState.eventType?.location
                            )
                        }
                    }
                }
            }

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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewAppbar(navigateBack: () -> Unit = {}) {
    TopAppBar(modifier = Modifier.fillMaxWidth(),
        title = { Text(stringResource(R.string.review_submit)) },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    painter = AppIcon.ArrowBack.getPainter(),
                    contentDescription = "navigate back"
                )
            }
        })
}