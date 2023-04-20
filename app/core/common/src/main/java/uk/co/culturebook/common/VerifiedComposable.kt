package uk.co.culturebook.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppIcon

@Composable
fun VerifiedComposable(modifier: Modifier = Modifier) {
    var showVerifiedDialog by remember { mutableStateOf(false) }
    if (showVerifiedDialog) {
        AlertDialog(
            onDismissRequest = { showVerifiedDialog = false },
            title = { Text(stringResource(R.string.verified)) },
            text = { Text(stringResource(R.string.verified_message)) },
            confirmButton = {
                TextButton(onClick = { showVerifiedDialog = false }) {
                    Text(stringResource(R.string.close))
                }
            }
        )
    }
    IconButton(modifier = modifier, onClick = { showVerifiedDialog = true }) {
        Icon(AppIcon.Sparkle.getPainter(), "verified")
    }
}