package uk.co.culturebook.ui.theme.molecules

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import uk.co.culturebook.ui.theme.*


@Composable
@Preview
fun SecondarySurfaceWithIcon(
    modifier: Modifier = Modifier,
    title: String = "Title",
    maxLines: Int = 1,
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
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis
                )
            }

            icon()
        }
    }
}

@Composable
@Preview
fun OutlinedColumnSurface(
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    title: String = "Title",
    maxLines: Int = 1
) {
    Surface(
        modifier = modifier,
        shape = mediumRoundedShape,
        color = Color.Transparent,
        border = BorderStroke(xxsSize, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier.padding(mediumSize),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            icon?.let {
                it()
                Spacer(modifier = Modifier.padding(bottom = mediumSize))
            }
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
@Preview
fun OutlinedRowSurface(
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    title: String = "Title",
    maxLines: Int = 1
) {
    Surface(
        modifier = modifier,
        shape = mediumRoundedShape,
        color = Color.Transparent,
        border = BorderStroke(xxsSize, MaterialTheme.colorScheme.outline)
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
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis
                )
            }

            icon?.invoke()
        }
    }
}
