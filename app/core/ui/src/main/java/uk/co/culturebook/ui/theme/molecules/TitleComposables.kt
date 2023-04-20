package uk.co.culturebook.ui.theme.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
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
    maxTitleLines: Int = Int.MAX_VALUE,
    titleContent: (@Composable () -> Unit)? = null,
    leadingTitleContent: (@Composable () -> Unit)? = null
) {
    val titleStyle = when (titleType) {
        TitleType.Large -> MaterialTheme.typography.titleLarge
        TitleType.Medium -> MaterialTheme.typography.titleMedium
        TitleType.Small -> MaterialTheme.typography.titleSmall
    }
    val subtitleStyle = when (titleType) {
        TitleType.Large -> MaterialTheme.typography.bodyMedium
        TitleType.Medium -> MaterialTheme.typography.bodyMedium
        TitleType.Small -> MaterialTheme.typography.bodySmall
    }

    Column(modifier = modifier) {
        if (leadingTitleContent != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    leadingTitleContent()
                    Text(
                        modifier = Modifier
                            .padding(start = smallSize)
                            .fillMaxWidth(0.7f),
                        text = title,
                        style = titleStyle,
                        maxLines = maxTitleLines,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.weight(1.0f))
                titleContent?.invoke()
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = titleStyle,
                    maxLines = maxTitleLines,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1.0f))
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