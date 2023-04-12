package uk.co.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.*
import uk.co.culturebook.data.location.registerForLocationUpdates
import uk.co.culturebook.data.location.unregisterLocationUpdates
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppIcon

@OptIn(ExperimentalPermissionsApi::class)
val fineLocationGranted @Composable get() = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION).status.isGranted

@OptIn(ExperimentalPermissionsApi::class)
val coarseLocationOnly @Composable get() = rememberPermissionState(android.Manifest.permission.ACCESS_COARSE_LOCATION).status.isGranted && !fineLocationGranted

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AskForLocationPermission(forceAsk: Boolean = false, onDismiss: () -> Unit = {}) {
    val permissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
    val coursePermission =
        rememberPermissionState(android.Manifest.permission.ACCESS_COARSE_LOCATION)

    val fineAccess = permissionState.status.isGranted
    val courseAccess = coursePermission.status.isGranted

    var showPermissionPrompt by remember { mutableStateOf(!fineAccess && !courseAccess) }

    if (showPermissionPrompt || forceAsk) {
        PermissionRequester(
            permissionState = permissionState,
            onDenied = {
                showPermissionPrompt = false
                onDismiss()
            },
            title = stringResource(R.string.location_permission_title),
            message = stringResource(R.string.location_permission),
            messageRationale = stringResource(R.string.location_permission_rationale),
            icon = {
                Icon(painter = AppIcon.Pin.getPainter(), contentDescription = "pin")
            }
        )
    }
}

@Composable
fun RegisterLocationChanges() {
    val accessGranted = fineLocationGranted || coarseLocationOnly
    val context = LocalContext.current

    DisposableEffect(accessGranted) {

        if (accessGranted) {
            registerForLocationUpdates(context)
        } else {
            unregisterLocationUpdates(context)
        }

        onDispose {
            unregisterLocationUpdates(context)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequester(
    permissionState: PermissionState,
    icon: (@Composable () -> Unit)? = null,
    title: String,
    message: String,
    messageRationale: String,
    onDenied: () -> Unit
) {
    if (!permissionState.status.isGranted) {
        val textToShow = if (permissionState.status.shouldShowRationale) {
            stringResource(R.string.location_permission_rationale)
            messageRationale
        } else {
            stringResource(R.string.location_permission)
            message
        }

        AlertDialog(
            title = { Text(title) },
            icon = icon,
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
