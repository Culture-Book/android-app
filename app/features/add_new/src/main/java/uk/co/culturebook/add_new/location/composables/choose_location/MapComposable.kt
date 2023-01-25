package uk.co.culturebook.add_new.location.composables.choose_location

import android.Manifest
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import uk.co.culturebook.ui.theme.AppIcon
import uk.co.culturebook.ui.theme.mapUiSettings
import uk.co.culturebook.ui.theme.mediumPadding
import uk.co.culturebook.ui.theme.xxlSize

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GoogleMapComposable(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    onMyLocationClicked: (Boolean) -> Unit
) {
    val locationPermission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    BoxWithConstraints(modifier = modifier) {
        Icon(
            modifier = Modifier
                .align(Alignment.Center)
                .zIndex(1f)
                .size(xxlSize),
            painter = AppIcon.Pin.getPainter(),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = "Pin"
        )
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = mapUiSettings
        ) {
            // TODO - Get the cultures in real time and show the geometry
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(mediumPadding),
            onClick = { onMyLocationClicked(locationPermission.status.isGranted) }) {
            Icon(
                painter = if (locationPermission.status.isGranted) AppIcon.MyLocation.getPainter() else AppIcon.LocationOff.getPainter(),
                contentDescription = "My location icon"
            )
        }
    }
}