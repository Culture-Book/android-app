package uk.co.culturebook.add_new.info.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.google.maps.android.compose.rememberCameraPositionState
import uk.co.culturebook.add_new.data.InfoData
import uk.co.culturebook.add_new.location.composables.choose_location.LocationBody
import uk.co.culturebook.data.models.cultural.ElementType
import uk.co.culturebook.data.models.cultural.Location
import uk.co.culturebook.data.models.cultural.MediaFile
import uk.co.culturebook.data.models.cultural.isNotEmpty
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.*
import uk.co.culturebook.ui.theme.molecules.*
import uk.co.culturebook.ui.utils.prettyPrint
import java.time.LocalDateTime

@Composable
fun AddInfoBody(
    modifier: Modifier = Modifier,
    infoData: InfoData = InfoData(),
    onLinkElementsClicked: () -> Unit = {},
    onDateAdded: (LocalDateTime) -> Unit = {},
    onLocationAdded: (Location) -> Unit = {},
    onAddFilesClicked: () -> Unit = {},
    onDeleteFile: (MediaFile) -> Unit,
    onSubmitClicked: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    var background by rememberSaveable { mutableStateOf(infoData.background) }
    var showDateCalendar by remember { mutableStateOf(false) }
    var showMap by remember { mutableStateOf(false) }

    if (showDateCalendar) {
        DateTimeDialog(
            pickedDate = infoData.eventType?.startDateTime,
            onDateChanged = {
                onDateAdded(it)
                showDateCalendar = false
            },
            onDismiss = {
                showDateCalendar = false
            }
        )
    }

    if (showMap) {
        val cameraPositionState = rememberCameraPositionState()
        LocationBody(
            modifier = modifier,
            cameraPositionState = cameraPositionState,
            onLocationSelected = {
                onLocationAdded(it)
                showMap = false
            },
            onBack = { showMap = false }
        )
    } else {
        Column(
            modifier = modifier
                .verticalScroll(scrollState)
        ) {
            TitleAndSubtitle(
                title = stringResource(R.string.background),
                message = stringResource(R.string.background_message)
            )

            LargeDynamicRoundedTextField(
                modifier = Modifier
                    .defaultMinSize(minHeight = xxxxlSize)
                    .padding(bottom = mediumSize)
                    .fillMaxWidth(),
                value = background,
                onValueChange = { background = it })

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = onLinkElementsClicked,
                enabled = false
            ) {
                // TODO - Implement linking when the nearby screen is done
                Text(stringResource(R.string.link_elements))
            }

            if (infoData.elementType == ElementType.Event.name) {
                TitleAndSubtitle(
                    modifier = Modifier.padding(vertical = smallSize),
                    title = stringResource(R.string.event_info),
                    message = stringResource(R.string.event_info_message)
                )

                if (infoData.eventType?.startDateTime != LocalDateTime.MIN && infoData.eventType != null) {
                    OutlinedSurface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = smallSize),
                        title = stringResource(
                            id = R.string.event_date,
                            infoData.eventType?.startDateTime?.prettyPrint() ?: ""
                        )
                    )
                }

                if (infoData.eventType?.location.isNotEmpty() && infoData.eventType != null) {
                    LocationBody(
                        modifier = Modifier
                            .padding(bottom = mediumSize)
                            .clip(mediumRoundedShape)
                            .height(xxxxlSize)
                            .fillMaxWidth(),
                        isDisplayOnly = true,
                        locationToShow = infoData.eventType?.location
                    )
                }

                EventTypeComposable(
                    onDateClicked = { showDateCalendar = true },
                    onLocation = { showMap = true }
                )

            }

            TitleAndSubtitle(
                modifier = Modifier.padding(top = mediumSize),
                title = stringResource(R.string.media),
                message = stringResource(R.string.media_message),
                titleContent = {
                    TextButton(
                        onClick = onAddFilesClicked
                    ) {
                        Icon(
                            painter = AppIcon.Add.getPainter(),
                            contentDescription = "Plus icon"
                        )
                        Text(
                            modifier = Modifier.padding(start = smallSize),
                            text = stringResource(R.string.add_files)
                        )
                    }
                }
            )

            LazyRow {
                items(infoData.files) {
                    if (it.contentType.contains("image")) {
                        ImageComposable(
                            modifier = Modifier
                                .size(height = xxxxlSize, width = xxxxlSize * 1.5f)
                                .padding(end = mediumSize),
                            uri = it.uri,
                            icon = {
                                Icon(
                                    painter = AppIcon.Bin.getPainter(),
                                    contentDescription = "Delete file"
                                )
                            },
                            onButtonClicked = { onDeleteFile(it) }
                        )
                    } else if (it.contentType.contains("video")) {
                        VideoComposable(
                            modifier = Modifier
                                .size(height = xxxxlSize, width = xxxxlSize * 1.5f)
                                .padding(end = mediumSize),
                            uri = it.uri,
                            icon = {
                                Icon(
                                    painter = AppIcon.Bin.getPainter(),
                                    contentDescription = "Delete file"
                                )
                            },
                            onButtonClicked = { onDeleteFile(it) }
                        )
                    } else if (it.contentType.contains("audio")) {
                        AudioComposable(
                            modifier = Modifier
                                .size(height = xxxxlSize, width = xxxxlSize * 1.5f)
                                .padding(end = mediumSize),
                            uri = it.uri,
                            icon = {
                                Icon(
                                    painter = AppIcon.Bin.getPainter(),
                                    contentDescription = "Delete file"
                                )
                            },
                            onButtonClicked = { onDeleteFile(it) }
                        )
                    }
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = mediumSize),
                onClick = { onSubmitClicked(background) }) {
                Text(stringResource(R.string.next))
            }
        }
    }
}