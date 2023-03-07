package uk.co.culturebook.details

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Popup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import kotlinx.coroutines.delay
import uk.co.common.*
import uk.co.common.choose_location.LocationBody
import uk.co.culturebook.data.Constants
import uk.co.culturebook.data.models.cultural.*
import uk.co.culturebook.data.remote_config.RemoteConfig
import uk.co.culturebook.data.utils.toUri
import uk.co.culturebook.nav.Route.Details.elementParam
import uk.co.culturebook.nav.fromJsonString
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.*
import uk.co.culturebook.ui.theme.molecules.LargeDynamicRoundedTextField
import uk.co.culturebook.ui.theme.molecules.OutlinedSurface
import uk.co.culturebook.ui.theme.molecules.TitleAndSubtitle
import uk.co.culturebook.ui.utils.prettyPrint

@Composable
fun DetailsScreenRoute(navController: NavController) {
    val viewModel = viewModel {
        val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
        DetailsViewModel()
    }
    val element = navController.currentBackStackEntry?.arguments?.getString(elementParam)
        ?.fromJsonString<Element>() ?: return

    DetailsScreenComposable(onBack = { navController.navigateUp() }, element = element)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreenComposable(
    onBack: () -> Unit,
    element: Element,
) {
    val scrollState = rememberScrollState()

    val token = remember { Firebase.remoteConfig.getString(RemoteConfig.MediaToken.key).trim() }
    val apiKey = remember { Firebase.remoteConfig.getString(RemoteConfig.MediaApiKey.key).trim() }

    Scaffold(
        topBar = { DetailsAppbar(element, onBack) },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = mediumSize)
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val media = element.media.firstOrNull()

            if (media?.isImage() == true) {
                ImageComposable(
                    modifier = Modifier
                        .height(xxxxlSize * 1.5f)
                        .fillMaxWidth(),
                    uri = media.uri.toUri(),
                )
            } else if (media?.isVideo() == true) {
                VideoComposable(
                    modifier = Modifier
                        .height(xxxxlSize)
                        .fillMaxWidth(),
                    uri = media.uri.toUri(),
                    headers = mapOf(
                        Constants.AuthorizationHeader to Constants.getBearerValue(token),
                        Constants.ApiKeyHeader to apiKey
                    )
                )
            } else if (media?.isAudio() == true) {
                AudioComposable(
                    modifier = Modifier
                        .size(height = xxxxlSize, width = xxxxlSize * 1.5f),
                    uri = media.uri.toUri(),
                )
            }

            TitleAndSubtitle(
                modifier = Modifier
                    .padding(top = mediumSize)
                    .fillMaxWidth(),
                title = stringResource(R.string.description)
            )

            LargeDynamicRoundedTextField(
                modifier = Modifier
                    .defaultMinSize(minHeight = xxxxlSize)
                    .padding(vertical = mediumSize)
                    .fillMaxWidth(),
                value = element.information,
                readOnly = true
            )

            if (element.type == ElementType.Event) {
                TitleAndSubtitle(
                    modifier = Modifier.padding(vertical = smallSize),
                    title = stringResource(R.string.event_info)
                )

                if (element.eventType?.startDateTime != null) {
                    OutlinedSurface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = smallSize),
                        title = stringResource(
                            id = R.string.event_date,
                            element.eventType?.startDateTime?.prettyPrint() ?: ""
                        )
                    )
                }

                if (element.eventType?.location != null) {
                    LocationBody(
                        modifier = Modifier
                            .padding(bottom = mediumSize)
                            .clip(mediumRoundedShape)
                            .height(xxxxlSize)
                            .fillMaxWidth(),
                        isDisplayOnly = true,
                        locationToShow = element.eventType?.location
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                FilledTonalButton(
                    modifier = Modifier
                        .fillMaxWidth(.5f)
                        .padding(end = smallSize),
                    onClick = { /*TODO*/ }) {
                    Text(text = stringResource(id = R.string.contributions))
                }
                FilledTonalButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = { /*TODO*/ }) {
                    Text(text = stringResource(id = R.string.directions))
                }
            }

            if (element.media.size > 1) {
                TitleAndSubtitle(
                    modifier = Modifier.padding(bottom = mediumSize),
                    title = stringResource(R.string.media)
                )

                LazyRow {
                    items(element.media) {
                        if (it.isImage()) {
                            ImageComposable(
                                modifier = Modifier
                                    .size(height = xxxxlSize, width = xxxxlSize * 1.5f)
                                    .padding(end = mediumSize),
                                uri = it.uri.toUri(),
                            )
                        } else if (it.isVideo()) {
                            VideoComposable(
                                modifier = Modifier
                                    .size(height = xxxxlSize, width = xxxxlSize * 1.5f)
                                    .padding(end = mediumSize),
                                uri = it.uri.toUri(),
                                headers = mapOf(
                                    Constants.AuthorizationHeader to Constants.getBearerValue(token),
                                    Constants.ApiKeyHeader to apiKey
                                )
                            )
                        } else if (it.isAudio()) {
                            AudioComposable(
                                modifier = Modifier
                                    .size(height = xxxxlSize, width = xxxxlSize * 1.5f)
                                    .padding(end = mediumSize),
                                uri = it.uri.toUri(),
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsAppbar(
    element: Element,
    navigateBack: () -> Unit = {},
    onFavouriteClicked: () -> Unit = {},
    onOptionsClicked: (ElementOptionsState) -> Unit = {}
) {
    TopAppBar(modifier = Modifier.fillMaxWidth(),
        actions = {
            var expanded by remember { mutableStateOf(false) }
            IconButton(onClick = { /*TODO*/ }) {
                if (element.favourite) {
                    Icon(AppIcon.FavouriteFilled.getPainter(), contentDescription = "fav")
                } else {
                    Icon(AppIcon.FavouriteOutline.getPainter(), contentDescription = "fav")
                }
            }
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
        },
        title = {
            var showPopup by remember { mutableStateOf(false) }
            Text(
                modifier = Modifier.clickable { showPopup = !showPopup },
                text = element.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (showPopup) {
                Popup(onDismissRequest = { showPopup = false }) {
                    LaunchedEffect(Unit) {
                        delay(element.name.length * 100L)
                        showPopup = false
                    }

                    Surface(
                        tonalElevation = smallSize,
                        shape = largeRoundedShape,
                        modifier = Modifier.padding(largeSize)
                    ) {
                        Text(
                            modifier = Modifier.padding(mediumSize),
                            text = element.name
                        )
                    }
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    painter = AppIcon.Close.getPainter(),
                    contentDescription = "navigate back"
                )
            }
        })
}