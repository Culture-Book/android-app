package uk.co.culturebook.add_new.location.composables.choose_location

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppIcon

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionWrapper(
    permissionState: PermissionState,
    onDenied: () -> Unit
) {
    if (!permissionState.status.isGranted) {
        val textToShow = if (permissionState.status.shouldShowRationale) {
            stringResource(R.string.location_permission_rationale)
        } else {
            stringResource(R.string.location_permission)
        }

        AlertDialog(
            title = { Text(stringResource(R.string.location_permission_title)) },
            icon = {
                Icon(
                    painter = AppIcon.Pin.getPainter(),
                    contentDescription = "Location icon"
                )
            },
            text = { Text(textToShow) },
            confirmButton = {
                TextButton(onClick = { permissionState.launchPermissionRequest() }) {
                    Text(stringResource(R.string.request_permission))
                }
            },
            dismissButton = {
                TextButton(onClick = { onDenied() }) {
                    Text(stringResource(R.string.close))
                }
            },
            onDismissRequest = { onDenied() })
    }
}