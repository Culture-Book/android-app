package uk.co.culturebook.ui.theme.molecules

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import uk.co.culturebook.ui.theme.mediumSize

@Composable
fun TitleAndSubtitle(
    modifier: Modifier = Modifier,
    title: String,
    message: String? = null,
    titleContent: @Composable () -> Unit = {}
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            titleContent()
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