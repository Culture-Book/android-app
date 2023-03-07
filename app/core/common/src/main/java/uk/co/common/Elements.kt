package uk.co.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import uk.co.culturebook.data.Constants
import uk.co.culturebook.data.models.cultural.*
import uk.co.culturebook.data.remote_config.RemoteConfig
import uk.co.culturebook.data.utils.toUri
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.*
import uk.co.culturebook.ui.theme.molecules.LoadingComposable
import uk.co.culturebook.ui.theme.molecules.TitleAndSubtitle
import java.util.*

@Composable
fun ShowElements(
    modifier: Modifier = Modifier,
    elements: List<Element>,
    onElementClicked: (Element) -> Unit,
    onOptionsClicked: (ElementOptionsState) -> Unit,
    onFavouriteClicked: (UUID) -> Unit,
    lastComposable: @Composable () -> Unit
) {
    LazyColumn(modifier = modifier) {
        item {
            TitleAndSubtitle(
                modifier = Modifier.padding(mediumSize),
                title = stringResource(id = R.string.elements)
            )
        }
        if (elements.isEmpty()) {
            item {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(mediumSize),
                        text = "No elements found"
                    )
                }
            }
        } else {
            items(elements) {
                ElementComposable(
                    modifier = Modifier.padding(horizontal = mediumSize, vertical = smallSize),
                    element = it,
                    onElementClicked = onElementClicked,
                    onOptionsClicked = onOptionsClicked,
                    onFavouriteClicked = onFavouriteClicked
                )
            }
        }
        item {
            lastComposable()
        }
    }
}

@Composable
fun ShowContributions(
    modifier: Modifier = Modifier,
    contributions: List<Contribution>,
    onClicked: (Contribution) -> Unit,
    onOptionsClicked: (ElementOptionsState) -> Unit,
    onFavouriteClicked: (UUID) -> Unit,
    lastComposable: @Composable () -> Unit
) {
    LazyColumn(modifier = modifier) {
        item {
            TitleAndSubtitle(
                modifier = Modifier.padding(mediumSize),
                title = stringResource(id = R.string.contributions)
            )
        }
        if (contributions.isEmpty()) {
            item {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(mediumSize),
                        text = "No contributions found"
                    )
                }
            }
        } else {
            items(contributions) {
                ContributionComposable(
                    modifier = Modifier.padding(horizontal = mediumSize, vertical = smallSize),
                    contribution = it,
                    onClicked = onClicked,
                    onOptionsClicked = onOptionsClicked,
                    onFavouriteClicked = onFavouriteClicked
                )
            }
        }
        item {
            lastComposable()
        }
    }
}

@Composable
fun ShowCultures(
    modifier: Modifier = Modifier,
    cultures: List<Culture>,
    onClicked: (Culture) -> Unit,
    onOptionsClicked: (ElementOptionsState) -> Unit,
    onFavouriteClicked: (UUID) -> Unit,
    lastComposable: @Composable () -> Unit
) {
    LazyColumn(modifier = modifier) {
        item {
            TitleAndSubtitle(
                modifier = Modifier.padding(mediumSize),
                title = stringResource(id = R.string.cultures)
            )
        }
        if (cultures.isEmpty()) {
            item {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(mediumSize),
                        text = "No cultures found"
                    )
                }
            }
        } else {
            items(cultures) {
                CultureComposable(
                    modifier = Modifier.padding(horizontal = mediumSize, vertical = smallSize),
                    culture = it,
                    onClicked = onClicked,
                    onOptionsClicked = onOptionsClicked,
                    onFavouriteClicked = onFavouriteClicked
                )
            }
        }
        item {
            lastComposable()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElementComposable(
    modifier: Modifier = Modifier,
    element: Element,
    onElementClicked: (Element) -> Unit,
    onFavouriteClicked: (UUID) -> Unit = {},
    onOptionsClicked: (ElementOptionsState) -> Unit
) {
    Card(
        modifier = modifier,
        shape = mediumRoundedShape,
        onClick = { onElementClicked(element) }
    ) {
        val media = element.media.firstOrNull()
        val imageLoader = rememberImageLoader()

        if (media?.isImage() == true) {
            var showLoading by remember { mutableStateOf(false) }
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = fiveXlSize)
            ) {
                if (showLoading) {
                    LoadingComposable()
                }
                AsyncImage(
                    modifier = Modifier.size(maxWidth, maxHeight),
                    imageLoader = imageLoader,
                    model = media.uri.toUri(),
                    contentScale = ContentScale.FillBounds,
                    filterQuality = FilterQuality.Low,
                    contentDescription = "image preview",
                    onState = {
                        showLoading = it is AsyncImagePainter.State.Loading
                    }
                )
            }
        }
        if (media?.isVideo() == true) {
            val token =
                remember { Firebase.remoteConfig.getString(RemoteConfig.MediaToken.key).trim() }
            val apiKey =
                remember { Firebase.remoteConfig.getString(RemoteConfig.MediaApiKey.key).trim() }
            val thumbnail = rememberVideoThumbnail(
                uri = media.uri.toUri(), headers = mapOf(
                    Constants.AuthorizationHeader to Constants.getBearerValue(token),
                    Constants.ApiKeyHeader to apiKey
                )
            )

            if (thumbnail != null) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = fiveXlSize),
                    painter = BitmapPainter(thumbnail),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "thumbnail"
                )
            } else {
                LoadingComposable()
            }
        }

        Surface(modifier = Modifier.fillMaxWidth(), tonalElevation = xsSize) {
            TitleAndSubtitle(
                modifier = Modifier.padding(mediumSize),
                title = element.name,
                message = element.information,
                maxMessageLines = 2,
                maxTitleLines = 1,
                leadingTitleContent = {
                    Icon(element.type.icon, "type icon")
                },
                titleContent = {
                    var expanded by remember { mutableStateOf(false) }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                            IconButton(onClick = { onFavouriteClicked(element.id!!) }) {
                                if (element.favourite) {
                                    Icon(
                                        AppIcon.FavouriteFilled.getPainter(),
                                        contentDescription = "fav"
                                    )
                                } else {
                                    Icon(
                                        AppIcon.FavouriteOutline.getPainter(),
                                        contentDescription = "fav"
                                    )
                                }
                            }
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "options")
                            }
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    expanded = false
                                    onOptionsClicked(ElementOptionsState.Hide(id = element.id!!))
                                },
                                text = { Text(stringResource(R.string.hide)) }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    expanded = false
                                    onOptionsClicked(ElementOptionsState.Report(id = element.id!!))
                                },
                                text = { Text(stringResource(R.string.report)) }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    expanded = false
                                    onOptionsClicked(ElementOptionsState.Block(id = element.id!!))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContributionComposable(
    modifier: Modifier = Modifier,
    contribution: Contribution,
    onClicked: (Contribution) -> Unit,
    onFavouriteClicked: (UUID) -> Unit = {},
    onOptionsClicked: (ElementOptionsState) -> Unit
) {
    Card(
        modifier = modifier,
        shape = mediumRoundedShape,
        onClick = { onClicked(contribution) }
    ) {
        val media = contribution.media.firstOrNull()
        val imageLoader = rememberImageLoader()

        if (media?.isImage() == true) {
            var showLoading by remember { mutableStateOf(false) }
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = fiveXlSize)
            ) {
                if (showLoading) {
                    LoadingComposable()
                }
                AsyncImage(
                    modifier = Modifier.size(maxWidth, maxHeight),
                    imageLoader = imageLoader,
                    model = media.uri.toUri(),
                    contentScale = ContentScale.FillBounds,
                    filterQuality = FilterQuality.Low,
                    contentDescription = "image preview",
                    onState = {
                        showLoading = it is AsyncImagePainter.State.Loading
                    }
                )
            }
        }
        if (media?.isVideo() == true) {
            val token = remember { Firebase.remoteConfig.getString(RemoteConfig.MediaToken.key) }
            val apiKey = remember { Firebase.remoteConfig.getString(RemoteConfig.MediaApiKey.key) }
            val thumbnail = rememberVideoThumbnail(
                uri = media.uri.toUri(), headers = mapOf(
                    Constants.AuthorizationHeader to Constants.getBearerValue(token),
                    Constants.ApiKeyHeader to apiKey
                )
            )

            if (thumbnail != null) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = fiveXlSize),
                    painter = BitmapPainter(thumbnail),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "thumbnail"
                )
            } else {
                LoadingComposable()
            }
        }

        Surface(modifier = Modifier.fillMaxWidth(), tonalElevation = xsSize) {
            TitleAndSubtitle(
                modifier = Modifier.padding(mediumSize),
                title = contribution.name,
                message = contribution.information,
                maxMessageLines = 2,
                maxTitleLines = 1,
                leadingTitleContent = {
                    Icon(contribution.type.icon, "type icon")
                },
                titleContent = {
                    var expanded by remember { mutableStateOf(false) }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                            IconButton(onClick = { onFavouriteClicked(contribution.id!!) }) {
                                if (contribution.favourite) {
                                    Icon(
                                        AppIcon.FavouriteFilled.getPainter(),
                                        contentDescription = "fav"
                                    )
                                } else {
                                    Icon(
                                        AppIcon.FavouriteOutline.getPainter(),
                                        contentDescription = "fav"
                                    )
                                }
                            }
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "options")
                            }
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    expanded = false
                                    onOptionsClicked(ElementOptionsState.Hide(id = contribution.id!!))
                                },
                                text = { Text(stringResource(R.string.hide)) }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    expanded = false
                                    onOptionsClicked(ElementOptionsState.Report(id = contribution.id!!))
                                },
                                text = { Text(stringResource(R.string.report)) }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    expanded = false
                                    onOptionsClicked(ElementOptionsState.Block(id = contribution.id!!))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CultureComposable(
    modifier: Modifier = Modifier,
    culture: Culture,
    onClicked: (Culture) -> Unit,
    onFavouriteClicked: (UUID) -> Unit = {},
    onOptionsClicked: (ElementOptionsState) -> Unit
) {
    Card(
        modifier = modifier,
        shape = mediumRoundedShape,
        onClick = { onClicked(culture) }
    ) {
        Surface(modifier = Modifier.fillMaxWidth(), tonalElevation = xsSize) {
            TitleAndSubtitle(
                modifier = Modifier.padding(mediumSize),
                title = culture.name,
                maxTitleLines = 1,
                titleContent = {
                    var expanded by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = { onFavouriteClicked(culture.id!!) }) {
                            if (culture.favourite) {
                                Icon(
                                    AppIcon.FavouriteFilled.getPainter(),
                                    contentDescription = "fav"
                                )
                            } else {
                                Icon(
                                    AppIcon.FavouriteOutline.getPainter(),
                                    contentDescription = "fav"
                                )
                            }
                        }

                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "options")

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    onClick = {
                                        expanded = false
                                        onOptionsClicked(ElementOptionsState.Hide(id = culture.id!!))
                                    },
                                    text = { Text(stringResource(R.string.hide)) }
                                )
                                DropdownMenuItem(
                                    onClick = {
                                        expanded = false
                                        onOptionsClicked(ElementOptionsState.Report(id = culture.id!!))
                                    },
                                    text = { Text(stringResource(R.string.report)) }
                                )
                                DropdownMenuItem(
                                    onClick = {
                                        expanded = false
                                        onOptionsClicked(ElementOptionsState.Block(id = culture.id!!))
                                    },
                                    text = { Text(stringResource(R.string.block)) }
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}

sealed interface ElementOptionsState {
    data class Hide(val id: UUID) : ElementOptionsState
    data class Report(val id: UUID) : ElementOptionsState
    data class Block(val id: UUID) : ElementOptionsState
}