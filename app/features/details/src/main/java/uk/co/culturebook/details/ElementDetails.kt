package uk.co.culturebook.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import uk.co.common.AudioComposable
import uk.co.common.ImageComposable
import uk.co.common.VideoComposable
import uk.co.common.choose_location.LocationBody
import uk.co.common.icon
import uk.co.culturebook.data.Constants
import uk.co.culturebook.data.models.cultural.*
import uk.co.culturebook.data.remote_config.RemoteConfig
import uk.co.culturebook.data.utils.toUri
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.*
import uk.co.culturebook.ui.theme.molecules.LargeDynamicRoundedTextField
import uk.co.culturebook.ui.theme.molecules.OutlinedSurface
import uk.co.culturebook.ui.theme.molecules.TitleAndSubtitle
import uk.co.culturebook.ui.theme.molecules.TitleType
import uk.co.culturebook.ui.utils.prettyPrint
import java.time.LocalDateTime
import java.util.*

@Composable
fun ElementDetailScreen(
    modifier: Modifier,
    element: Element
) {
    with(element) {
        DetailScreen(
            modifier = modifier,
            type = type,
            name = name,
            information = information,
            media = media,
            linkElements = linkElements,
            eventType = eventType
        )
    }
}

@Composable
fun ContributionDetailScreen(
    modifier: Modifier,
    contribution: Contribution
) {
    with(contribution) {
        DetailScreen(
            modifier = modifier,
            type = type,
            name = name,
            information = information,
            media = media,
            linkElements = linkElements,
            eventType = eventType,
            isContribution = true
        )
    }
}

@Composable
fun DetailScreen(
    modifier: Modifier,
    type: ElementType,
    name: String,
    information: String,
    media: List<Media> = emptyList(),
    linkElements: List<UUID> = emptyList(),
    eventType: EventType? = null,
    isContribution: Boolean = false
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
                painter = type.icon,
                contentDescription = "type icon"
            )
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge
            )
        }

        val localMedia = media.firstOrNull()
        if (localMedia?.isImage() == true) {
            ImageComposable(
                modifier = Modifier
                    .height(xxxxlSize * 1.5f)
                    .fillMaxWidth(),
                uri = localMedia.uri.toUri(),
            )
        } else if (localMedia?.isVideo() == true) {
            VideoComposable(
                modifier = Modifier
                    .height(xxxxlSize)
                    .fillMaxWidth(),
                uri = localMedia.uri.toUri(),
                headers = mapOf(
                    Constants.AuthorizationHeader to Constants.getBearerValue(token),
                    Constants.ApiKeyHeader to apiKey
                )
            )
        } else if (localMedia?.isAudio() == true) {
            AudioComposable(
                modifier = Modifier
                    .size(height = xxxxlSize, width = xxxxlSize * 1.5f),
                uri = localMedia.uri.toUri(),
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
            value = information,
            readOnly = true
        )

        if (linkElements.isNotEmpty()) {
            TitleAndSubtitle(
                modifier = Modifier.padding(vertical = smallSize),
                title = stringResource(R.string.linked_elements, linkElements.size),
                titleType = TitleType.Small
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            if (!isContribution) {
                FilledTonalButton(
                    modifier = Modifier
                        .fillMaxWidth(.5f)
                        .padding(end = smallSize),
                    onClick = { /*TODO*/ }) {
                    Text(text = stringResource(id = R.string.contributions))
                }
            }
            FilledTonalButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { /*TODO*/ }) {
                Text(text = stringResource(id = R.string.directions))
            }
        }

        if (type == ElementType.Event) {
            TitleAndSubtitle(
                modifier = Modifier.padding(vertical = smallSize),
                title = stringResource(R.string.event_info)
            )

            if (eventType != null) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    if (eventType.startDateTime != LocalDateTime.MIN) {
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
                            title = eventType.startDateTime.prettyPrint()
                        )
                    }

                    if (eventType.location.isNotEmpty()) {
                        LocationBody(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(.5f)
                                .clip(mediumRoundedShape)
                                .height(xxxxlSize),
                            isDisplayOnly = true,
                            locationToShow = eventType.location
                        )
                    }
                }
            }
        }

        if (media.size > 1) {
            TitleAndSubtitle(
                modifier = Modifier.padding(bottom = mediumSize),
                title = stringResource(R.string.media)
            )

            LazyRow {
                items(media) {
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