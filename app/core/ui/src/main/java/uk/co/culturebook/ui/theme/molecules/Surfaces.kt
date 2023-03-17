package uk.co.culturebook.ui.theme.molecules

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import uk.co.culturebook.ui.theme.mediumRoundedShape
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.smallSize
import uk.co.culturebook.ui.theme.xxsSize


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
    icon: (@Composable () -> Unit)? = null,
    title: String = "Title",
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
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
