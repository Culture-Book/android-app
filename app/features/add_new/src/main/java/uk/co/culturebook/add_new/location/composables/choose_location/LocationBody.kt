package uk.co.culturebook.add_new.location.composables.choose_location

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.CameraPositionState
import uk.co.culturebook.add_new.location.events.LocationEvent
import uk.co.culturebook.data.location.LocationFlow
import uk.co.culturebook.data.location.LocationStatus
import uk.co.culturebook.data.models.cultural.isEmpty
import uk.co.culturebook.data.models.cultural.isNotEmpty
import uk.co.culturebook.data.models.cultural.toLatLng
import uk.co.culturebook.data.models.cultural.toLocation
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.molecules.TitleAndSubtitle


@Composable
fun LocationBody(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    postEvent: (LocationEvent) -> Unit,
) {
    var animate by remember { mutableStateOf(false) }
    val userLocation by LocationFlow.collectAsState()
    var latLng by remember { mutableStateOf(cameraPositionState.position.target) }

    LaunchedEffect(userLocation) {
        val locationStatus = userLocation as? LocationStatus.Success
        val newLatLng = locationStatus?.location?.toLatLng()
        if (newLatLng != null) {
            if (latLng.isEmpty() && newLatLng.isNotEmpty()) {
                animate = true
            }
            latLng = newLatLng
        }
    }

    LaunchedEffect(animate) {
        if (animate) {
            val cameraPosition = CameraPosition.fromLatLngZoom(latLng, 17f)
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
            cameraPositionState.animate(cameraUpdate, 300)
        }
        animate = false
    }

    Column(modifier = modifier) {
        TitleAndSubtitle(
            title = stringResource(R.string.location_title),
            message = stringResource(R.string.location_subtitle)
        )

        GoogleMapComposable(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.9f),
            cameraPositionState = cameraPositionState,
            onMyLocationClicked = { animate = it })

        Button(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(mediumSize),
            onClick = {
                val currentLocation = cameraPositionState.position.target
                postEvent(LocationEvent.GetCultures(currentLocation.toLocation()))
            }) {
            Text(stringResource(R.string.select_location))
        }

    }
}