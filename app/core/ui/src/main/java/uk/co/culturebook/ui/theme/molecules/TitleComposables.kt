package uk.co.culturebook.ui.theme.molecules

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.smallSize

enum class TitleType {
    Large, Medium, Small
}

@Composable
fun TitleAndSubtitle(
    modifier: Modifier = Modifier,
    title: String,
    message: String? = null,
    titleType: TitleType = TitleType.Large,
    maxMessageLines: Int = Int.MAX_VALUE,
    titleContent: (@Composable () -> Unit)? = null,
    leadingTitleContent: (@Composable () -> Unit)? = null
) {
    val titleStyle = when(titleType) {
        TitleType.Large -> MaterialTheme.typography.titleLarge
        TitleType.Medium -> MaterialTheme.typography.titleMedium
        TitleType.Small -> MaterialTheme.typography.titleSmall
    }
    val subtitleStyle = when(titleType) {
        TitleType.Large -> MaterialTheme.typography.bodyMedium
        TitleType.Medium -> MaterialTheme.typography.bodyMedium
        TitleType.Small -> MaterialTheme.typography.bodySmall
    }

    Column(modifier = modifier) {
        if (leadingTitleContent != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                leadingTitleContent()
                Text(
                    modifier = Modifier.padding(start = smallSize),
                    text = title,
                    style = titleStyle
                )
                titleContent?.invoke()
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = titleStyle
                )
                titleContent?.invoke()
            }
        }


        if (message != null) {
            Text(
                modifier = Modifier.padding(top = smallSize),
                text = message,
                style = subtitleStyle,
                maxLines = maxMessageLines,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}