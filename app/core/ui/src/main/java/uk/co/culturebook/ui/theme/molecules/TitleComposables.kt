package uk.co.culturebook.ui.theme.molecules

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.mediumSize

@Composable
fun TitleAndSubtitle(title: String, message: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge
    )
    Text(
        modifier = Modifier.padding(vertical = mediumSize),
        text = message,
        style = MaterialTheme.typography.bodyMedium
    )
}