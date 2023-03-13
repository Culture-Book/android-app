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
import uk.co.culturebook.data.repositories.cultural.NearbyRepository
import uk.co.culturebook.data.repositories.cultural.UpdateRepository
import uk.co.culturebook.data.utils.toUri
import uk.co.culturebook.nav.Route.Details.elementParam
import uk.co.culturebook.nav.fromJsonString
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.*
import uk.co.culturebook.ui.theme.molecules.*
import uk.co.culturebook.ui.utils.ShowSnackbar
import uk.co.culturebook.ui.utils.prettyPrint
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreenRoute(navController: NavController) {
    val viewModel = viewModel {
        val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
        val updateRepository = UpdateRepository(app)
        val nearbyRepository = NearbyRepository(app)
        DetailsViewModel(nearbyRepository, updateRepository)
    }
    val snackbarState = remember { SnackbarHostState() }
    val state by viewModel.detailStateFlow.collectAsState()
    var element by remember {
        mutableStateOf(
            navController.currentBackStackEntry?.arguments?.getString(elementParam)
                ?.fromJsonString<Element>()
        )
    }

    LaunchedEffect(state) {
        when (state) {
            is DetailState.ElementReceived -> element =
                (state as DetailState.ElementReceived).element
            is DetailState.Blocked -> {
                navController.navigateUp()
            }
            else -> {}
        }
    }

    if (state is DetailState.Error)
        ShowSnackbar(stringId = (state as DetailState.Error).error, snackbarState = snackbarState)

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarState) },
        topBar = {
            DetailsAppbar(
                navigateBack = { navController.navigateUp() },
                isFavourite = element?.favourite ?: false,
                onFavouriteClicked = {
                    element?.let {
                        viewModel.postEvent(DetailEvent.FavouriteElement(it))
                    }
                },
                onBlock = {
                    element?.let {
                        viewModel.postEvent(DetailEvent.BlockElement(it.id))
                    }
                },
                onHide = {
                    element?.let {
                        viewModel.postEvent(DetailEvent.BlockElement(it.id))
                    }
                },
                onReport = {
                    element?.let {
                        viewModel.postEvent(DetailEvent.BlockElement(it.id))
                    }
                }
            )
        },
    ) { padding ->
        when {
            state is DetailState.Loading -> LoadingComposable(padding)
            element != null -> ElementDetailScreen(Modifier.padding(padding), element!!)
        }
    }
}

@Composable
fun ElementDetailScreen(
    modifier: Modifier,
    element: Element,
) {
    val scrollState = rememberScrollState()

    val token = remember { Firebase.remoteConfig.getString(RemoteConfig.MediaToken.key).trim() }
    val apiKey =
        remember { Firebase.remoteConfig.getString(RemoteConfig.MediaApiKey.key).trim() }

    Column(
        modifier = modifier
            .padding(horizontal = mediumSize)
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val media = element.media.firstOrNull()

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = mediumSize),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${stringResource(R.string.title)}: ",
                style = MaterialTheme.typography.titleLarge
            )
            Icon(
                modifier = Modifier.padding(horizontal = smallSize),
                painter = element.type.icon,
                contentDescription = "type icon"
            )
            Text(
                text = element.name,
                style = MaterialTheme.typography.titleLarge
            )
        }

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

        if (element.linkElements.isNotEmpty()) {
            TitleAndSubtitle(
                modifier = Modifier.padding(bottom = smallSize),
                title = stringResource(R.string.linked_elements, element.linkElements.size),
                titleType = TitleType.Small
            )
        }

        if (element.type == ElementType.Event) {
            TitleAndSubtitle(
                modifier = Modifier.padding(vertical = smallSize),
                title = stringResource(R.string.event_info)
            )

            if (element.eventType != null) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    if (element.eventType?.startDateTime != LocalDateTime.MIN) {
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
                            title = element.eventType?.startDateTime?.prettyPrint() ?: ""
                        )
                    }

                    if (element.eventType?.location.isNotEmpty()) {
                        LocationBody(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(.5f)
                                .clip(mediumRoundedShape)
                                .height(xxxxlSize),
                            isDisplayOnly = true,
                            locationToShow = element.eventType?.location
                        )
                    }
                }
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
                                Constants.AuthorizationHeader to Constants.getBearerValue(
                                    token
                                ),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsAppbar(
    title: String = stringResource(R.string.details),
    navigateBack: () -> Unit = {},
    onFavouriteClicked: () -> Unit = {},
    isFavourite: Boolean = false,
    onHide: () -> Unit = {},
    onBlock: () -> Unit = {},
    onReport: () -> Unit = {}
) {
    TopAppBar(modifier = Modifier.fillMaxWidth(),
        actions = {
            var expanded by remember { mutableStateOf(false) }
            IconButton(onClick = { onFavouriteClicked() }) {
                if (isFavourite) {
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
                            onHide()
                        },
                        text = { Text(stringResource(R.string.hide)) }
                    )
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            onReport()
                        },
                        text = { Text(stringResource(R.string.report)) }
                    )
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            onBlock()
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
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (showPopup) {
                Popup(onDismissRequest = { showPopup = false }) {
                    LaunchedEffect(Unit) {
                        delay(title.length * 100L)
                        showPopup = false
                    }

                    Surface(
                        tonalElevation = smallSize,
                        shape = largeRoundedShape,
                        modifier = Modifier.padding(largeSize)
                    ) {
                        Text(
                            modifier = Modifier.padding(mediumSize),
                            text = title
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