package uk.co.common.choose_location

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import uk.co.culturebook.data.location.LocationFlow
import uk.co.culturebook.data.location.LocationStatus
import uk.co.culturebook.data.models.cultural.Location
import uk.co.culturebook.data.models.cultural.isEmpty
import uk.co.culturebook.data.models.cultural.isNotEmpty
import uk.co.culturebook.data.models.cultural.latLng
import uk.co.culturebook.data.models.cultural.location
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.molecules.TitleAndSubtitle
import uk.co.culturebook.ui.utils.disableGestures


@Composable
fun LocationBody(
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    onLocationSelected: (Location) -> Unit = {},
    onBack: (() -> Unit)? = null,
    isDisplayOnly: Boolean = false,
    locationToShow: Location? = null
) {
    var animate by remember { mutableStateOf(false) }
    val locationState by LocationFlow.collectAsState()
    val location = locationToShow ?: (locationState as? LocationStatus.Success)?.location
    var latLng by remember { mutableStateOf(cameraPositionState.position.target) }

    LaunchedEffect(location) {
        val newLatLng = location?.latLng
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
        if (!title.isNullOrEmpty() && !subtitle.isNullOrEmpty()) {
            TitleAndSubtitle(
                modifier = Modifier.padding(bottom = mediumSize),
                title = title,
                message = subtitle
            )
        }

        GoogleMapComposable(
            modifier = Modifier
                .disableGestures(isDisplayOnly)
                .fillMaxSize()
                .weight(0.9f),
            cameraPositionState = cameraPositionState,
            onMyLocationClicked = { animate = it },
            isDisplayOnly = isDisplayOnly
        )

        if (!isDisplayOnly) {
            Button(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(top = mediumSize),
                onClick = {
                    val currentLatLng = cameraPositionState.position.target
                    onLocationSelected(currentLatLng.location)
                }) {
                Text(stringResource(R.string.select_location))
            }

            if (onBack != null) {
                OutlinedButton(
                    modifier = Modifier
                        .padding(vertical = mediumSize)
                        .fillMaxWidth(),
                    onClick = { onBack() }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        }

    }
}