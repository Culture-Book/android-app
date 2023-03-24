package uk.co.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import uk.co.culturebook.ui.theme.molecules.TitleType
import java.util.*

@Composable
fun ShowElements(
    modifier: Modifier = Modifier,
    elements: List<Element> = emptyList(),
    selectedElements: List<Element> = emptyList(),
    showTitle: Boolean = true,
    onEditClicked: ((Element) -> Unit)? = null,
    onElementClicked: (Element) -> Unit = {},
    onOptionsClicked: ((BlockOptionsState) -> Unit)? = null,
    onFavouriteClicked: ((UUID) -> Unit)? = null,
    lastComposable: @Composable () -> Unit
) {
    LazyColumn(modifier = modifier) {
        if (showTitle) {
            item {
                TitleAndSubtitle(
                    modifier = Modifier.padding(mediumSize),
                    title = stringResource(id = R.string.elements)
                )
            }
        }
        if (elements.isEmpty()) {
            item {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(mediumSize), text = "No elements found"
                    )
                }
            }
        } else {
            items(elements) {
                val isSelected = selectedElements.contains(it)
                ElementComposable(
                    modifier = Modifier.padding(horizontal = mediumSize, vertical = smallSize),
                    element = it,
                    onElementClicked = onElementClicked,
                    onOptionsClicked = onOptionsClicked,
                    onFavouriteClicked = onFavouriteClicked,
                    isSelected = isSelected,
                    onEditClicked = onEditClicked
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
    onEditClicked: ((Contribution) -> Unit)? = null,
    onOptionsClicked: ((BlockOptionsState) -> Unit)? = null,
    onFavouriteClicked: ((UUID) -> Unit)? = null,
    lastComposable: (@Composable () -> Unit)? = null
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
                        modifier = Modifier.padding(mediumSize), text = "No contributions found"
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
                    onFavouriteClicked = onFavouriteClicked,
                    onEditClicked = onEditClicked
                )
            }
        }
        item {
            lastComposable?.invoke()
        }
    }
}

@Composable
fun ShowCultures(
    modifier: Modifier = Modifier,
    cultures: List<Culture>,
    onClicked: (Culture) -> Unit,
    onEditClicked: ((Culture) -> Unit)? = null,
    onOptionsClicked: ((BlockOptionsState) -> Unit)? = null,
    onFavouriteClicked: ((UUID) -> Unit)? = null,
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
                        modifier = Modifier.padding(mediumSize), text = "No cultures found"
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
                    onFavouriteClicked = onFavouriteClicked,
                    onEditClicked = onEditClicked
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
    isSelected: Boolean = false,
    onElementClicked: (Element) -> Unit,
    onEditClicked: ((Element) -> Unit)? = null,
    onFavouriteClicked: ((UUID) -> Unit)? = null,
    onOptionsClicked: ((BlockOptionsState) -> Unit)? = null
) {
    Card(modifier = modifier, shape = mediumRoundedShape, onClick = { onElementClicked(element) }) {
        val media = element.media.firstOrNull()
        val imageLoader = rememberImageLoader()
        val backgroundColor =
            if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        val contentColor =
            if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface

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
                AsyncImage(modifier = Modifier.size(maxWidth, maxHeight),
                    imageLoader = imageLoader,
                    model = media.uri.toUri(),
                    contentScale = ContentScale.FillBounds,
                    filterQuality = FilterQuality.Low,
                    contentDescription = "image preview",
                    onState = {
                        showLoading = it is AsyncImagePainter.State.Loading
                    })
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

        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = xsSize,
            color = backgroundColor,
            contentColor = contentColor
        ) {
            Column(modifier = Modifier.padding(mediumSize)) {
                TitleAndSubtitle(
                    title = element.name,
                    message = element.information,
                    maxMessageLines = 2,
                    maxTitleLines = 1,
                    leadingTitleContent = {
                        if (isSelected) {
                            Icon(AppIcon.Tick.getPainter(), "tick")
                        } else {
                            Icon(element.type.icon, "type icon")
                        }
                    },
                    titleContent = {
                        var expanded by remember { mutableStateOf(false) }
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                                if (onEditClicked != null) {
                                    IconButton(onClick = { onEditClicked(element) }) {
                                        Icon(
                                            AppIcon.Bin.getPainter(),
                                            contentDescription = "Bin"
                                        )
                                    }
                                }

                                if (onFavouriteClicked != null) {
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
                                }
                                if (onOptionsClicked != null) {
                                    IconButton(onClick = { expanded = true }) {
                                        Icon(
                                            AppIcon.MoreVert.getPainter(),
                                            contentDescription = "options"
                                        )
                                    }
                                }
                            }
                            if (onOptionsClicked != null) {
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }) {
                                    DropdownMenuItem(onClick = {
                                        expanded = false
                                        onOptionsClicked(BlockOptionsState.Hide(id = element.id!!))
                                    }, text = { Text(stringResource(R.string.hide)) })
                                    DropdownMenuItem(onClick = {
                                        expanded = false
                                        onOptionsClicked(BlockOptionsState.Report(id = element.id!!))
                                    }, text = { Text(stringResource(R.string.report)) })
                                    DropdownMenuItem(onClick = {
                                        expanded = false
                                        onOptionsClicked(BlockOptionsState.Block(id = element.id!!))
                                    }, text = { Text(stringResource(R.string.block)) })
                                }
                            }
                        }
                    })

                if (element.isVerified) {
                    TitleAndSubtitle(
                        modifier = Modifier.padding(top = smallSize),
                        title = stringResource(R.string.verified),
                        titleType = TitleType.Medium,
                        leadingTitleContent = {
                            Icon(AppIcon.Sparkle.getPainter(), "verified")
                        })
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContributionComposable(
    modifier: Modifier = Modifier,
    contribution: Contribution,
    onClicked: (Contribution) -> Unit,
    onEditClicked: ((Contribution) -> Unit)? = null,
    onFavouriteClicked: ((UUID) -> Unit)? = null,
    onOptionsClicked: ((BlockOptionsState) -> Unit)? = null
) {
    Card(modifier = modifier, shape = mediumRoundedShape, onClick = { onClicked(contribution) }) {
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
                AsyncImage(modifier = Modifier.size(maxWidth, maxHeight),
                    imageLoader = imageLoader,
                    model = media.uri.toUri(),
                    contentScale = ContentScale.FillBounds,
                    filterQuality = FilterQuality.Low,
                    contentDescription = "image preview",
                    onState = {
                        showLoading = it is AsyncImagePainter.State.Loading
                    })
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
            Column(modifier = Modifier.padding(mediumSize)) {
                TitleAndSubtitle(
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
                                if (onEditClicked != null) {
                                    IconButton(onClick = { onEditClicked(contribution) }) {
                                        Icon(
                                            AppIcon.Bin.getPainter(),
                                            contentDescription = "Bin"
                                        )
                                    }
                                }

                                if (onFavouriteClicked != null) {
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
                                }
                                if (onOptionsClicked != null) {
                                    IconButton(onClick = { expanded = true }) {
                                        Icon(
                                            AppIcon.MoreVert.getPainter(),
                                            contentDescription = "options"
                                        )
                                    }
                                }
                            }
                            if (onOptionsClicked != null) {
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }) {
                                    DropdownMenuItem(onClick = {
                                        expanded = false
                                        onOptionsClicked(BlockOptionsState.Hide(id = contribution.id!!))
                                    }, text = { Text(stringResource(R.string.hide)) })
                                    DropdownMenuItem(onClick = {
                                        expanded = false
                                        onOptionsClicked(BlockOptionsState.Report(id = contribution.id!!))
                                    }, text = { Text(stringResource(R.string.report)) })
                                    DropdownMenuItem(onClick = {
                                        expanded = false
                                        onOptionsClicked(BlockOptionsState.Block(id = contribution.id!!))
                                    }, text = { Text(stringResource(R.string.block)) })
                                }
                            }
                        }
                    })

                if (contribution.isVerified) {
                    TitleAndSubtitle(
                        modifier = Modifier.padding(top = smallSize),
                        title = stringResource(R.string.verified),
                        titleType = TitleType.Medium,
                        leadingTitleContent = {
                            Icon(AppIcon.Sparkle.getPainter(), "verified")
                        })
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CultureComposable(
    modifier: Modifier = Modifier,
    culture: Culture,
    onClicked: (Culture) -> Unit,
    onEditClicked: ((Culture) -> Unit)? = null,
    onFavouriteClicked: ((UUID) -> Unit)? = null,
    onOptionsClicked: ((BlockOptionsState) -> Unit)? = null
) {
    Card(modifier = modifier, shape = mediumRoundedShape, onClick = { onClicked(culture) }) {
        Surface(modifier = Modifier.fillMaxWidth(), tonalElevation = xsSize) {
            TitleAndSubtitle(modifier = Modifier.padding(mediumSize),
                title = culture.name,
                maxTitleLines = 1,
                titleContent = {
                    var expanded by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                    ) {
                        if (onEditClicked != null) {
                            IconButton(onClick = { onEditClicked(culture) }) {
                                Icon(
                                    AppIcon.Bin.getPainter(),
                                    contentDescription = "Bin"
                                )
                            }
                        }

                        if (onFavouriteClicked != null) {
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
                        }

                        if (onOptionsClicked != null) {
                            IconButton(onClick = { expanded = true }) {
                                Icon(AppIcon.MoreVert.getPainter(), contentDescription = "options")

                                DropdownMenu(expanded = expanded,
                                    onDismissRequest = { expanded = false }) {
                                    DropdownMenuItem(onClick = {
                                        expanded = false
                                        onOptionsClicked(BlockOptionsState.Hide(id = culture.id!!))
                                    }, text = { Text(stringResource(R.string.hide)) })
                                    DropdownMenuItem(onClick = {
                                        expanded = false
                                        onOptionsClicked(BlockOptionsState.Report(id = culture.id!!))
                                    }, text = { Text(stringResource(R.string.report)) })
                                    DropdownMenuItem(onClick = {
                                        expanded = false
                                        onOptionsClicked(BlockOptionsState.Block(id = culture.id!!))
                                    }, text = { Text(stringResource(R.string.block)) })
                                }
                            }
                        }
                    }
                })
        }
    }
}