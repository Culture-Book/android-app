package uk.co.culturebook.ui.theme.molecules

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.smallSize

@Composable
fun TitleAndSubtitle(
    modifier: Modifier = Modifier,
    title: String,
    message: String? = null,
    titleContent: (@Composable () -> Unit)? = null,
    leadingTitleContent: (@Composable () -> Unit)? = null
) {
    Column(modifier = modifier) {
        if (leadingTitleContent != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                leadingTitleContent()
                Text(
                    modifier = Modifier.padding(start = smallSize),
                    text = title,
                    style = MaterialTheme.typography.titleLarge
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
                    style = MaterialTheme.typography.titleLarge
                )
                titleContent?.invoke()
            }
        }


        if (message != null) {
            Text(
                modifier = Modifier.padding(vertical = mediumSize),
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}