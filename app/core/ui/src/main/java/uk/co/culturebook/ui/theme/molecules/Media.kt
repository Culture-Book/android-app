package uk.co.culturebook.ui.theme.molecules

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size
import uk.co.culturebook.ui.theme.*

@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
private fun getExoPlayer(uri: Uri): ExoPlayer {
    val context = LocalContext.current
    return remember {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                val defaultDataSourceFactory = DefaultDataSource.Factory(context)
                val dataSourceFactory: DataSource.Factory =
                    DefaultDataSource.Factory(context, defaultDataSourceFactory)
                val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(uri))
                setMediaSource(source)
                prepare()
            }
    }
}

@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun MediaPlayer(
    modifier: Modifier = Modifier,
    uri: Uri,
    contentType: String
) {
    val exoPlayer = getExoPlayer(uri = uri)

    exoPlayer.playWhenReady = true
    exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
    exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

    DisposableEffect(
        AndroidView(
            modifier = modifier,
            factory = {
                PlayerView(it).apply {
                    resizeMode = RESIZE_MODE_ZOOM
                    player = exoPlayer
                    layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                }
            })
    ) {
        onDispose { exoPlayer.release() }
    }
}

@Composable
fun ImageComposable(
    modifier: Modifier,
    uri: Uri,
    icon: @Composable (() -> Unit)? = null,
    onButtonClicked: () -> Unit = {}
) {
    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(uri)
        .size(Size.ORIGINAL)
        .scale(Scale.FIT)
        .crossfade(true)
        .build()
    val painter = rememberAsyncImagePainter(model = imageRequest)
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            BoxWithConstraints {
                when (painter.state) {
                    is AsyncImagePainter.State.Loading -> LoadingComposable()
                    AsyncImagePainter.State.Empty, is AsyncImagePainter.State.Success -> {
                        Image(
                            modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.Center)
                                .clip(mediumRoundedShape),
                            contentScale = ContentScale.FillWidth,
                            painter = painter,
                            contentDescription = "large media image",
                        )
                    }
                    is AsyncImagePainter.State.Error -> {
                        Icon(
                            modifier = Modifier.align(Alignment.Center),
                            painter = AppIcon.BrokenImage.getPainter(),
                            contentDescription = "broken image"
                        )
                    }
                }
            }
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .clip(mediumRoundedShape)
            .border(xxsSize, MaterialTheme.colorScheme.outline, mediumRoundedShape)
            .clickable { showDialog = true }
    ) {
        when (painter.state) {
            is AsyncImagePainter.State.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            AsyncImagePainter.State.Empty -> {}
            is AsyncImagePainter.State.Success,
            is AsyncImagePainter.State.Error -> {
                if (icon != null) {
                    FilledTonalIconButton(
                        modifier = Modifier
                            .padding(smallSize)
                            .align(Alignment.TopEnd)
                            .zIndex(1f),
                        onClick = onButtonClicked
                    ) {
                        icon()
                    }
                }
            }
        }

        when (painter.state) {
            is AsyncImagePainter.State.Loading -> LoadingComposable()
            AsyncImagePainter.State.Empty, is AsyncImagePainter.State.Success -> {
                Image(
                    painter = painter,
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
            }
            is AsyncImagePainter.State.Error -> {
                Icon(
                    modifier = Modifier.align(Alignment.Center),
                    painter = AppIcon.BrokenImage.getPainter(),
                    contentDescription = "broken image"
                )
            }
        }
    }
}

@Composable
fun VideoComposable(
    modifier: Modifier,
    uri: Uri,
    icon: @Composable (() -> Unit)? = null,
    onButtonClicked: () -> Unit = {}
) {
    val context = LocalContext.current
    val mediaMetadata = remember { MediaMetadataRetriever().apply { setDataSource(context, uri) } }
    val imageBitmap = remember { mediaMetadata.frameAtTime?.asImageBitmap() }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            BoxWithConstraints(
                modifier = Modifier
                    .height(xxxxlSize * 3f)
            ) {
                if (imageBitmap != null) {
                    MediaPlayer(
                        uri = uri, contentType = "video"
                    )
                } else {
                    Icon(
                        modifier = Modifier.align(Alignment.Center),
                        painter = AppIcon.BrokenImage.getPainter(),
                        contentDescription = "broken image"
                    )
                }
            }
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .clip(mediumRoundedShape)
            .border(xxsSize, MaterialTheme.colorScheme.outline, mediumRoundedShape)
            .clickable { showDialog = true }
    ) {
        if (icon != null) {
            FilledTonalIconButton(
                modifier = Modifier
                    .padding(smallSize)
                    .align(Alignment.TopEnd)
                    .zIndex(1f),
                onClick = onButtonClicked
            ) {
                icon()
            }
        }

        if (imageBitmap != null) {
            Image(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center)
                    .clip(mediumRoundedShape),
                contentScale = ContentScale.FillWidth,
                bitmap = imageBitmap,
                contentDescription = "large media image",
            )
        } else {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                painter = AppIcon.BrokenImage.getPainter(),
                contentDescription = "broken image"
            )
        }
    }
}

@Composable
fun AudioComposable(
    modifier: Modifier,
    uri: Uri,
    icon: @Composable (() -> Unit)? = null,
    onButtonClicked: () -> Unit = {}
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            BoxWithConstraints(
                modifier = Modifier
                    .height(xxxxlSize * 3)
            ) {
                MediaPlayer(uri = uri, contentType = "audio")
            }
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .clip(mediumRoundedShape)
            .border(xxsSize, MaterialTheme.colorScheme.outline, mediumRoundedShape)
            .clickable { showDialog = true }
    ) {
        if (icon != null) {
            FilledTonalIconButton(
                modifier = Modifier
                    .padding(smallSize)
                    .align(Alignment.TopEnd)
                    .zIndex(1f),
                onClick = onButtonClicked
            ) {
                icon()
            }
        }

        Icon(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = mediumSize)
                .fillMaxWidth(),
            painter = AppIcon.Audio.getPainter(),
            contentDescription = "audio file"
        )
    }
}
