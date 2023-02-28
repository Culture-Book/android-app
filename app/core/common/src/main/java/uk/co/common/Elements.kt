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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import uk.co.culturebook.data.Constants
import uk.co.culturebook.data.models.cultural.Element
import uk.co.culturebook.data.models.cultural.isImage
import uk.co.culturebook.data.models.cultural.isVideo
import uk.co.culturebook.data.remote_config.RemoteConfig
import uk.co.culturebook.data.utils.toUri
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.*
import uk.co.culturebook.ui.theme.molecules.TitleAndSubtitle
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
                    Text(modifier = Modifier.align(Alignment.Center), text = "No elements found")
                }
            }
        } else {
            items(elements) {
                ElementComposable(
                    modifier = Modifier.padding(horizontal = mediumSize, vertical = smallSize),
                    element = it,
                    onElementClicked = onElementClicked,
                    onOptionsClicked = onOptionsClicked
                )
            }
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
        modifier = modifier,
        shape = mediumRoundedShape,
        onClick = { onElementClicked(element) }
    ) {
        val media = element.media.firstOrNull()
        val context = LocalContext.current
        val imageLoader = rememberImageLoader()

        if (media?.isImage() == true) {
            val request = ImageRequest.Builder(context)
                .data(media.uri)
                .build()

            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = fiveXlSize),
                model = request,
                contentScale = ContentScale.FillWidth,
                contentDescription = "image preview"
            )

            LaunchedEffect(request) {
                imageLoader.enqueue(request)
            }
        }
        if (media?.isVideo() == true) {
            val token = remember { Firebase.remoteConfig.getString(RemoteConfig.MediaToken.key) }
            val apiKey = remember { Firebase.remoteConfig.getString(RemoteConfig.MediaApiKey.key) }

            VideoComposable(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = fiveXlSize),
                uri = media.uri.toUri(),
                headers = mapOf(
                    Constants.AuthorizationHeader to Constants.getBearerValue(token),
                    Constants.ApiKeyHeader to apiKey
                ),
                enablePreview = false
            )
        }

        Surface(modifier = Modifier.fillMaxWidth(), tonalElevation = xsSize) {
            TitleAndSubtitle(
                modifier = Modifier.padding(mediumSize),
                title = element.name,
                message = element.information,
                maxMessageLines = 2,
                titleContent = {
                    var expanded by remember { mutableStateOf(false) }
                    Box(
                        modifier = Modifier
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
                                    expanded = false
                                    onOptionsClicked(ElementOptionsState.Hide(elementId = element.id!!))
                                },
                                text = { Text(stringResource(R.string.hide)) }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    expanded = false
                                    onOptionsClicked(ElementOptionsState.Report(elementId = element.id!!))
                                },
                                text = { Text(stringResource(R.string.report)) }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    expanded = false
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

sealed interface ElementOptionsState {
    data class Hide(val elementId: UUID) : ElementOptionsState
    data class Report(val elementId: UUID) : ElementOptionsState
    data class Block(val elementId: UUID) : ElementOptionsState
}