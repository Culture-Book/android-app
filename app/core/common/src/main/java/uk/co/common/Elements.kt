package uk.co.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import uk.co.culturebook.data.models.cultural.Element
import uk.co.culturebook.data.models.cultural.isImage
import uk.co.culturebook.data.models.cultural.isVideo
import uk.co.culturebook.data.utils.toUri
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.fiveXlSize
import uk.co.culturebook.ui.theme.mediumRoundedShape
import uk.co.culturebook.ui.theme.molecules.TitleAndSubtitle
import uk.co.culturebook.ui.theme.molecules.VideoComposable
import uk.co.culturebook.ui.theme.xsSize
import java.util.*

@Composable
fun ShowElements(
    modifier: Modifier = Modifier,
    elements: List<Element>,
    onElementClicked: (Element) -> Unit,
    onOptionsClicked: (ElementOptionsState) -> Unit
) {
    LazyColumn(modifier = modifier) {
        if (elements.isEmpty()) {
            item {
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

                }
            }
        }
        items(elements) {
            ElementComposable(
                element = it,
                onElementClicked = onElementClicked,
                onOptionsClicked = onOptionsClicked
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElementComposable(
    modifier: Modifier = Modifier,
    element: Element,
    onElementClicked: (Element) -> Unit,
    onOptionsClicked: (ElementOptionsState) -> Unit
) {
    Card(
        shape = mediumRoundedShape,
        onClick = { onElementClicked(element) }
    ) {
        BoxWithConstraints(modifier = modifier) {
            val media = element.media.first()

            if (media.isImage()) {
                AsyncImage(
                    modifier = Modifier
                        .width(maxWidth)
                        .heightIn(max = fiveXlSize),
                    model = media.uri,
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "image preview"
                )
            }
            if (media.isVideo()) {
                VideoComposable(
                    modifier = Modifier
                        .width(maxWidth)
                        .heightIn(max = fiveXlSize),
                    uri = media.uri.toUri(),
                    enablePreview = false
                )
            }

            Surface(modifier = Modifier.width(maxWidth), tonalElevation = xsSize) {
                TitleAndSubtitle(
                    title = element.name,
                    message = element.information,
                    maxMessageLines = 2,
                    titleContent = {
                        var expanded by remember { mutableStateOf(false) }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.TopStart)
                        ) {
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "options")
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    onClick = {
                                        onOptionsClicked(ElementOptionsState.Hide(elementId = element.id!!))
                                    },
                                    text = { Text(stringResource(R.string.hide)) }
                                )
                                DropdownMenuItem(
                                    onClick = {
                                        onOptionsClicked(ElementOptionsState.Report(elementId = element.id!!))
                                    },
                                    text = { Text(stringResource(R.string.report)) }
                                )
                                DropdownMenuItem(
                                    onClick = {
                                        onOptionsClicked(ElementOptionsState.Block(elementId = element.id!!))
                                    },
                                    text = { Text(stringResource(R.string.block)) }
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

sealed interface ElementOptionsState {
    data class Hide(val elementId: UUID) : ElementOptionsState
    data class Report(val elementId: UUID) : ElementOptionsState
    data class Block(val elementId: UUID) : ElementOptionsState
}