package uk.co.common.choose_location

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import uk.co.common.AskForLocationPermission
import uk.co.common.RegisterLocationChanges
import uk.co.common.coarseLocationOnly
import uk.co.common.fineLocationGranted
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppIcon
import uk.co.culturebook.ui.theme.mapUiSettings
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.xxlSize

@Composable
fun GoogleMapComposable(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    onMyLocationClicked: (Boolean) -> Unit,
    isDisplayOnly: Boolean = false
) {
    var askForPermission by remember { mutableStateOf(false) }
    val isLocationPermissionGranted = fineLocationGranted || coarseLocationOnly
    val mapProperties = googleMapProperties()

    if (askForPermission && !isLocationPermissionGranted) {
        AskForLocationPermission { askForPermission = false }
    }

    RegisterLocationChanges()

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
            uiSettings = mapUiSettings,
            properties = mapProperties
        ) {
            // TODO - Get the cultures in real time and show the geometry
        }

        if (!isDisplayOnly) {
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(mediumSize),
                onClick = {
                    if (!isLocationPermissionGranted) {
                        askForPermission = true
                    }
                    onMyLocationClicked(isLocationPermissionGranted)
                }
            ) {
                Icon(
                    painter = if (isLocationPermissionGranted) AppIcon.MyLocation.getPainter() else AppIcon.LocationOff.getPainter(),
                    contentDescription = "My location icon"
                )
            }
        }
    }
}

@Composable
fun googleMapProperties(): MapProperties {
    val context = LocalContext.current
    val isDarkMode = isSystemInDarkTheme()
    val mapStyleOptions =
        if (isDarkMode) MapStyleOptions.loadRawResourceStyle(context, R.raw.map_dark) else
            MapStyleOptions.loadRawResourceStyle(context, R.raw.map_light)

    return MapProperties(mapStyleOptions = mapStyleOptions)
}