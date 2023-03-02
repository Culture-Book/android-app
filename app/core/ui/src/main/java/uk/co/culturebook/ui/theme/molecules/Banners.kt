package uk.co.culturebook.ui.theme.molecules

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import uk.co.culturebook.ui.theme.AppIcon
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.smallSize

enum class BannerType {
    Warning, Error, Info
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Banner(
    modifier: Modifier = Modifier,
    bannerType: BannerType = BannerType.Info,
    title: String,
    message: String,
    onDismiss: () -> Unit,
    leadingIcon: (@Composable () -> Unit)? = null,
    onClick: () -> Unit = {}
) {
    val bannerColor = when (bannerType) {
        BannerType.Info -> MaterialTheme.colorScheme.surfaceColorAtElevation(smallSize)
        BannerType.Warning -> MaterialTheme.colorScheme.secondaryContainer
        BannerType.Error -> MaterialTheme.colorScheme.errorContainer
    }
    val bannerContentColor = when (bannerType) {
        BannerType.Info -> MaterialTheme.colorScheme.onSurface
        BannerType.Warning -> MaterialTheme.colorScheme.onSecondaryContainer
        BannerType.Error -> MaterialTheme.colorScheme.onErrorContainer
    }

    Surface(
        modifier = modifier,
        onClick = onClick,
        color = bannerColor,
        contentColor = bannerContentColor
    ) {
        Row(
            modifier = Modifier
                .padding(mediumSize)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            leadingIcon?.invoke()
            TitleAndSubtitle(
                modifier = Modifier.fillMaxWidth(0.8f),
                title = title,
                message = message,
                titleType = TitleType.Small
            )
            IconButton(onClick = onDismiss) {
                Icon(painter = AppIcon.Close.getPainter(), contentDescription = "close")
            }
        }
    }
}