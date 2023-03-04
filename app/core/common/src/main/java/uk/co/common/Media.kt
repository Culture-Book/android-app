package uk.co.common

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
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
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
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.disk.DiskCache
import coil.imageLoader
import coil.memory.MemoryCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.co.culturebook.data.logD
import uk.co.culturebook.data.remote.retrofit.imageLoaderClient
import uk.co.culturebook.data.utils.isValidContent
import uk.co.culturebook.data.utils.isValidHttp
import uk.co.culturebook.ui.theme.*
import uk.co.culturebook.ui.theme.molecules.LoadingComposable

@Composable
fun rememberImageLoader(): ImageLoader {
    val context = LocalContext.current
    return remember {
        context.imageLoader.newBuilder()
            .okHttpClient(imageLoaderClient)
            .crossfade(true)
            .error(AppIcon.BrokenImage.icon)
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .build()
    }
}

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
    uri: Uri
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
    onButtonClicked: () -> Unit = {},
    enablePreview: Boolean = true,
    filterQuality: FilterQuality = FilterQuality.High,
) {
    var isLoading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            if (isLoading) {
                CircularProgressIndicator()
            }

            AsyncImage(
                model = uri,
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center,
                onState = { state -> isLoading = state is AsyncImagePainter.State.Loading },
                filterQuality = filterQuality, contentDescription = "image"
            )
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .clip(mediumRoundedShape)
            .border(xxsSize, MaterialTheme.colorScheme.outline, mediumRoundedShape)
            .clickable(enablePreview) { showDialog = true }
    ) {
        if (icon != null && !isLoading) {
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

        if (isLoading) {
            LoadingComposable()
        }

        AsyncImage(
            modifier = Modifier.size(maxWidth, maxHeight),
            model = uri,
            contentScale = ContentScale.FillBounds,
            onState = { state -> isLoading = state is AsyncImagePainter.State.Loading },
            filterQuality = FilterQuality.Low,
            contentDescription = "image"
        )
    }
}

@Composable
        /**
         * Gets a video thumbnail from a uri, the way this works is the following:
         * We create a MediaMetadataRetriever which fetches the video, depending on whether it's http or content,
         * then we launch a coroutine that sets the data source and fetches the first frame.
         * Once a frame is found, we need to cancel the retrieving coroutine and stop it from fetching more data.
         * **/
fun rememberVideoThumbnail(uri: Uri, headers: Map<String, String> = mapOf()): ImageBitmap? {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val retriever = remember { MediaMetadataRetriever() }
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            retriever.use {
                try {
                    if (uri.isValidHttp()) {
                        retriever.setDataSource(uri.toString(), headers)
                        imageBitmap = retriever.frameAtTime?.asImageBitmap()
                    } else if (uri.isValidContent()) {
                        retriever.setDataSource(context, uri)
                        imageBitmap = retriever.frameAtTime?.asImageBitmap()
                    }
                } catch (e: IllegalStateException) {
                    "metadataReceiver was released.".logD("VideoThumbnail")
                }
            }
        }
    }

    return imageBitmap
}

@Composable
fun VideoComposable(
    modifier: Modifier,
    uri: Uri,
    headers: Map<String, String> = mapOf(),
    icon: @Composable (() -> Unit)? = null,
    onButtonClicked: () -> Unit = {},
    enablePreview: Boolean = true
) {
    val imageBitmap = rememberVideoThumbnail(uri = uri, headers = headers)
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            BoxWithConstraints(
                modifier = Modifier
                    .height(xxxxlSize * 3f)
            ) {
                if (imageBitmap != null) {
                    MediaPlayer(uri = uri)
                } else {
                    LoadingComposable()
                }
            }
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .clip(mediumRoundedShape)
            .border(xxsSize, MaterialTheme.colorScheme.outline, mediumRoundedShape)
            .clickable(enablePreview) { showDialog = true }
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
                MediaPlayer(uri = uri)
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
