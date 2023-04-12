package uk.co.culturebook.common

import androidx.compose.material3.*
import androidx.compose.runtime.*
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