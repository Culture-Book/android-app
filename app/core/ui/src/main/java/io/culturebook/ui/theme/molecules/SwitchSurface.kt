package io.culturebook.ui.theme.molecules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import io.culturebook.ui.theme.mediumPadding
import io.culturebook.ui.theme.mediumRoundedShape
import io.culturebook.ui.theme.smallPadding

@Composable
@Preview
fun TertiarySwitchSurface(
    modifier: Modifier = Modifier,
    title: String = "Title",
    subtitle: String = "Subtitle",
    checked: Boolean = false,
    onSwitchChanged: (Boolean) -> Unit = {},
    onSubtitleClicked: (() -> Unit)? = null
) {
    var check by remember { mutableStateOf(checked) }

    Surface(
        modifier = modifier,
        shape = mediumRoundedShape,
        color = MaterialTheme.colorScheme.tertiaryContainer
    ) {
        Row(
            modifier = Modifier.padding(mediumPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )

                Text(
                    modifier = if (onSubtitleClicked != null) {
                        Modifier
                            .padding(top = smallPadding)
                            .clickable { onSubtitleClicked() }
                    } else Modifier,
                    text = subtitle,
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    textDecoration = if (onSubtitleClicked != null) TextDecoration.Underline else null,
                )
            }

            Switch(checked = check, onCheckedChange = {
                check = it
                onSwitchChanged(it)
            })
        }
    }
}