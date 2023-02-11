package uk.co.culturebook.ui.theme.molecules

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size
import uk.co.culturebook.ui.theme.*

@Composable
@Preview
fun SecondarySurfaceWithIcon(
    modifier: Modifier = Modifier,
    title: String = "Title",
    icon: @Composable () -> Unit = {},
    onButtonClicked: () -> Unit = {}
) {
    Surface(
        modifier = modifier,
        shape = mediumRoundedShape,
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Row(
            modifier = Modifier.padding(vertical = smallSize, horizontal = mediumSize),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.fillMaxWidth(0.6f)) {
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(modifier = Modifier.fillMaxWidth(0.4f), onClick = onButtonClicked) {
                icon()
            }
        }
    }
}

@Composable
@Preview
fun OutlinedSurface(
    modifier: Modifier = Modifier,
    title: String = "Title",
) {
    Surface(
        modifier = modifier,
        shape = mediumRoundedShape,
        color = Color.Transparent,
        border = BorderStroke(xxsSize, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier.padding(mediumSize),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                title,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ImageWithIcon(
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