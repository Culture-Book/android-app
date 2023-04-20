package uk.co.culturebook.add_new.info.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.google.maps.android.compose.rememberCameraPositionState
import uk.co.common.choose_location.LocationBody
import uk.co.culturebook.add_new.data.AddNewState
import uk.co.culturebook.common.AudioComposable
import uk.co.culturebook.common.ImageComposable
import uk.co.culturebook.common.VideoComposable
import uk.co.culturebook.data.models.cultural.ElementType
import uk.co.culturebook.data.models.cultural.Location
import uk.co.culturebook.data.models.cultural.MediaFile
import uk.co.culturebook.data.models.cultural.isNotEmpty
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppIcon
import uk.co.culturebook.ui.theme.mediumRoundedShape
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.molecules.DateTimeDialog
import uk.co.culturebook.ui.theme.molecules.LargeDynamicRoundedTextField
import uk.co.culturebook.ui.theme.molecules.OutlinedColumnSurface
import uk.co.culturebook.ui.theme.molecules.TitleAndSubtitle
import uk.co.culturebook.ui.theme.molecules.TitleType
import uk.co.culturebook.ui.theme.smallSize
import uk.co.culturebook.ui.theme.xxxxlSize
import uk.co.culturebook.ui.utils.prettyPrint
import java.time.LocalDateTime

@Composable
fun AddInfoBody(
    modifier: Modifier = Modifier,
    addNewState: AddNewState,
    onLinkElementsClicked: () -> Unit = {},
    onDateAdded: (LocalDateTime) -> Unit = {},
    onLocationAdded: (Location) -> Unit = {},
    onAddFilesClicked: () -> Unit = {},
    onDeleteFile: (MediaFile) -> Unit,
    onSubmitClicked: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    var background by rememberSaveable { mutableStateOf(addNewState.information) }
    var showDateCalendar by remember { mutableStateOf(false) }
    var showMap by remember { mutableStateOf(false) }

    if (showDateCalendar) {
        DateTimeDialog(pickedDate = addNewState.eventType?.startDateTime, onDateChanged = {
            onDateAdded(it)
            showDateCalendar = false
        }, onDismiss = {
            showDateCalendar = false
        })
    }

    if (showMap) {
        val cameraPositionState = rememberCameraPositionState()
        LocationBody(modifier = modifier,
            cameraPositionState = cameraPositionState,
            onLocationSelected = {
                onLocationAdded(it)
                showMap = false
            },
            onBack = { showMap = false })
    } else {
        Column(
            modifier = modifier.verticalScroll(scrollState)
        ) {
            TitleAndSubtitle(
                modifier = Modifier.padding(bottom = mediumSize),
                title = stringResource(R.string.background),
                message = stringResource(R.string.background_message)
            )

            LargeDynamicRoundedTextField(modifier = Modifier
                .defaultMinSize(minHeight = xxxxlSize)
                .padding(bottom = mediumSize)
                .fillMaxWidth(),
                value = background,
                onValueChange = { background = it })

            if (addNewState.linkElements.isNotEmpty()) {
                TitleAndSubtitle(modifier = Modifier.padding(bottom = smallSize),
                    title = stringResource(R.string.linked_elements, addNewState.linkElements.size),
                    titleType = TitleType.Small,
                    titleContent = {
                        TextButton(onClick = { addNewState.linkElements = emptyList() }) {
                            Text(
                                modifier = Modifier.padding(end = smallSize),
                                text = stringResource(R.string.clear)
                            )
                        }
                    })
            }

            TextButton(
                modifier = Modifier.fillMaxWidth(), onClick = onLinkElementsClicked
            ) {
                if (addNewState.linkElements.isEmpty()) {
                    Text(stringResource(R.string.link_elements))
                } else {
                    Text(stringResource(R.string.link_more_elements))
                }
            }


            if (addNewState.type == ElementType.Event) {
                TitleAndSubtitle(
                    modifier = Modifier.padding(vertical = smallSize),
                    title = stringResource(R.string.event_info),
                    message = stringResource(R.string.event_info_message)
                )

                if (addNewState.eventType != null) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        if (addNewState.eventType?.startDateTime != LocalDateTime.MIN) {
                            OutlinedColumnSurface(
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

                EventTypeComposable(
                    onDateClicked = { showDateCalendar = true },
                    onLocation = { showMap = true }
                )

            }

            TitleAndSubtitle(modifier = Modifier.padding(vertical = mediumSize),
                title = stringResource(R.string.media),
                message = stringResource(R.string.media_message),
                titleContent = {
                    TextButton(
                        onClick = onAddFilesClicked
                    ) {
                        Icon(
                            painter = AppIcon.Add.getPainter(), contentDescription = "Plus icon"
                        )
                        Text(
                            modifier = Modifier.padding(start = smallSize),
                            text = stringResource(R.string.add_files)
                        )
                    }
                })

            LazyRow {
                items(addNewState.files) {
                    if (it.contentType.contains("image")) {
                        ImageComposable(modifier = Modifier
                            .size(
                                height = xxxxlSize, width = xxxxlSize * 1.5f
                            )
                            .padding(end = mediumSize), uri = it.uri, icon = {
                            Icon(
                                painter = AppIcon.Bin.getPainter(),
                                contentDescription = "Delete file"
                            )
                        }, onButtonClicked = { onDeleteFile(it) })
                    } else if (it.contentType.contains("video")) {
                        VideoComposable(modifier = Modifier
                            .size(
                                height = xxxxlSize, width = xxxxlSize * 1.5f
                            )
                            .padding(end = mediumSize), uri = it.uri, icon = {
                            Icon(
                                painter = AppIcon.Bin.getPainter(),
                                contentDescription = "Delete file"
                            )
                        }, onButtonClicked = { onDeleteFile(it) })
                    } else if (it.contentType.contains("audio")) {
                        AudioComposable(modifier = Modifier
                            .size(
                                height = xxxxlSize, width = xxxxlSize * 1.5f
                            )
                            .padding(end = mediumSize), uri = it.uri, icon = {
                            Icon(
                                painter = AppIcon.Bin.getPainter(),
                                contentDescription = "Delete file"
                            )
                        }, onButtonClicked = { onDeleteFile(it) })
                    }
                }
            }

            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = mediumSize),
                onClick = { onSubmitClicked(background) }) {
                Text(stringResource(R.string.next))
            }
        }
    }
}