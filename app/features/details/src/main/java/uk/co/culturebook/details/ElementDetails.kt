package uk.co.culturebook.details

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ShareCompat
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import uk.co.common.BlockOptionsState
import uk.co.common.choose_location.LocationBody
import uk.co.common.icon
import uk.co.culturebook.common.AudioComposable
import uk.co.culturebook.common.ImageComposable
import uk.co.culturebook.common.VideoComposable
import uk.co.culturebook.data.Constants
import uk.co.culturebook.data.models.cultural.Comment
import uk.co.culturebook.data.models.cultural.Contribution
import uk.co.culturebook.data.models.cultural.Element
import uk.co.culturebook.data.models.cultural.ElementType
import uk.co.culturebook.data.models.cultural.EventType
import uk.co.culturebook.data.models.cultural.Location
import uk.co.culturebook.data.models.cultural.Media
import uk.co.culturebook.data.models.cultural.Reaction
import uk.co.culturebook.data.models.cultural.isAudio
import uk.co.culturebook.data.models.cultural.isImage
import uk.co.culturebook.data.models.cultural.isNotEmpty
import uk.co.culturebook.data.models.cultural.isVideo
import uk.co.culturebook.data.remote_config.RemoteConfig
import uk.co.culturebook.data.utils.toUri
import uk.co.culturebook.nav.DeepLinks
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppIcon
import uk.co.culturebook.ui.theme.buttonFooter
import uk.co.culturebook.ui.theme.buttonHeader
import uk.co.culturebook.ui.theme.buttonInside
import uk.co.culturebook.ui.theme.mediumRoundedShape
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.molecules.LargeDynamicRoundedTextField
import uk.co.culturebook.ui.theme.molecules.OutlinedColumnSurface
import uk.co.culturebook.ui.theme.molecules.TitleAndSubtitle
import uk.co.culturebook.ui.theme.molecules.TitleType
import uk.co.culturebook.ui.theme.smallSize
import uk.co.culturebook.ui.theme.xsSize
import uk.co.culturebook.ui.theme.xxxxlSize
import uk.co.culturebook.ui.utils.prettyPrint
import java.time.LocalDateTime
import java.util.Collections
import java.util.UUID
import kotlin.math.roundToInt

@Composable
fun ElementDetailScreen(
    modifier: Modifier,
    comments: List<Comment>,
    element: Element,
    onCommentBlocked: (UUID) -> Unit,
    onCommentSent: (String) -> Unit,
    onContributionsClicked: (UUID) -> Unit,
    onAddReaction: (String) -> Unit,
    onDeleteComment: (UUID) -> Unit,
    onDeleteReaction: (String) -> Unit,
    onGetComments: (UUID) -> Unit
) {
    with(element) {
        DetailScreen(
            modifier = modifier,
            id = id!!,
            location = location,
            type = type,
            name = name,
            information = information,
            media = media,
            linkElements = linkElements,
            eventType = eventType,
            reactions = reactions,
            comments = comments,
            onCommentBlocked = onCommentBlocked,
            onCommentSent = onCommentSent,
            onContributionsClicked = onContributionsClicked,
            onReactionSelected = onAddReaction,
            onDeleteComment = onDeleteComment,
            onDeleteReaction = onDeleteReaction,
            onGetComments = onGetComments
        )
    }
}

@Composable
fun ContributionDetailScreen(
    modifier: Modifier,
    comments: List<Comment>,
    contribution: Contribution,
    onCommentBlocked: (UUID) -> Unit,
    onCommentSent: (String) -> Unit,
    onAddReaction: (String) -> Unit,
    onDeleteComment: (UUID) -> Unit,
    onDeleteReaction: (String) -> Unit,
    onGetComments: (UUID) -> Unit
) {
    with(contribution) {
        DetailScreen(
            modifier = modifier,
            id = id!!,
            location = location,
            type = type,
            name = name,
            information = information,
            media = media,
            linkElements = linkElements,
            eventType = eventType,
            isContribution = true,
            reactions = reactions,
            comments = comments,
            onCommentBlocked = onCommentBlocked,
            onCommentSent = onCommentSent,
            onReactionSelected = onAddReaction,
            onDeleteComment = onDeleteComment,
            onDeleteReaction = onDeleteReaction,
            onGetComments = onGetComments,
        )
    }
}

@Composable
fun DetailScreen(
    modifier: Modifier,
    id: UUID,
    location: Location,
    type: ElementType,
    name: String,
    information: String,
    media: List<Media> = emptyList(),
    linkElements: List<UUID> = emptyList(),
    eventType: EventType? = null,
    isContribution: Boolean = false,
    reactions: List<Reaction> = emptyList(),
    onReactionSelected: (String) -> Unit = {},
    comments: List<Comment> = emptyList(),
    onCommentSent: (String) -> Unit = {},
    onCommentBlocked: (UUID) -> Unit,
    onDeleteComment: (UUID) -> Unit,
    onDeleteReaction: (String) -> Unit,
    onContributionsClicked: ((UUID) -> Unit)? = null,
    onGetComments: (UUID) -> Unit = {},
) {
    val context = LocalContext.current
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
                fillBounds = false
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
                headers = mapOf(
                    Constants.AuthorizationHeader to Constants.getBearerValue(token),
                    Constants.ApiKeyHeader to apiKey
                )
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
                    onClick = { onContributionsClicked?.invoke(id) }) {
                    Text(text = stringResource(id = R.string.contributions))
                }
            }
            FilledTonalButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    context.startActivity(
                        Intent(
                            ACTION_VIEW,
                            Uri.parse("google.navigation:q=${location.latitude},${location.longitude}")
                        ).apply {
                            setPackage("com.google.android.apps.maps")
                        }
                    )
                }) {
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
                            uri = it.uri.toUri()
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
                            headers = mapOf(
                                Constants.AuthorizationHeader to Constants.getBearerValue(token),
                                Constants.ApiKeyHeader to apiKey
                            )
                        )
                    }
                }
            }
        }

        val shareString = stringResource(R.string.share_string)
        var showReactionSheet by remember { mutableStateOf(false) }
        var showCommentSheet by remember { mutableStateOf(false) }
        var showReactionPopup by remember { mutableStateOf(false) }
        var buttonHeight by remember { mutableStateOf(0) }

        if (showReactionSheet) {
            EmojiSheet(
                selectedEmoji = reactions.find { it.isMine }?.reaction ?: "",
                onDismiss = {
                    showReactionSheet = false
                }
            ) {
                onReactionSelected(it)
                showReactionSheet = false
            }
        }

        if (showCommentSheet) {
            CommentSheet(
                onDismiss = { showCommentSheet = false },
                listComments = comments,
                onCommentSent = onCommentSent,
                onOptionSelected = {
                    when (it) {
                        is BlockOptionsState.Block -> onCommentBlocked(it.id)
                        is BlockOptionsState.Hide -> onCommentBlocked(it.id)
                        is BlockOptionsState.Report -> onCommentBlocked(it.id)
                    }
                },
                onDeleteComment = onDeleteComment
            )
        }

        Row(modifier = Modifier.padding(vertical = mediumSize)) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.33f)
                    .onGloballyPositioned {
                        buttonHeight = it.size.height
                    },
                shape = buttonHeader,
                onClick = { showReactionPopup = true }) {
                Icon(painter = AppIcon.Emoji.getPainter(), contentDescription = "reactions")
                Text(modifier = Modifier.padding(start = smallSize), text = "${reactions.size}")

                if (showReactionPopup) {
                    EmojiPopUp(
                        emojis = reactions.associateWith { reaction ->
                            Collections.frequency(
                                reactions,
                                reaction
                            )
                        },
                        selectedEmoji = reactions.find { it.isMine }?.reaction ?: "",
                        yOffset = -(buttonHeight * 1.5f).roundToInt(),
                        onDismiss = { showReactionPopup = false },
                        onAddCustomEmoji = { showReactionSheet = true },
                        onEmojiSelected = {
                            showReactionPopup = false
                            onReactionSelected(it)
                        },
                        onDelete = {
                            showReactionPopup = false
                            onDeleteReaction(it)
                        }
                    )
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = xsSize)
                    .weight(0.33f),
                shape = buttonInside,
                onClick = {
                    showCommentSheet = true
                    onGetComments(id)
                }) {
                Icon(painter = AppIcon.Comment.getPainter(), contentDescription = "comments")
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.33f),
                shape = buttonFooter,
                onClick = {
                    val link = DeepLinks.buildDetailsDeepLink(id, isContribution)
                    ShareCompat.IntentBuilder(context)
                        .setType("text/plain")
                        .setChooserTitle(R.string.share_string)
                        .setText("$shareString: $link")
                        .startChooser()
                }) {
                Icon(painter = AppIcon.Share.getPainter(), contentDescription = "share")
            }
        }
    }
}