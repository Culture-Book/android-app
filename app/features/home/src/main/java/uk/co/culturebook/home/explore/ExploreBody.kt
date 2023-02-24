package uk.co.culturebook.home.explore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import uk.co.common.AskForLocationPermission
import uk.co.common.courseLocationOnly
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppIcon
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.molecules.Banner

@Composable
fun ExploreBody(
    modifier: Modifier,
    filterState: FilterState,
    exploreState: ExploreState,
    postEvent: (ExploreEvent) -> Unit
) {
    Column(modifier) {
        ShowBanners()
    }
}

@Composable
fun ShowBanners() {
    val isCourseLocation = courseLocationOnly
    var showLocationBanner by remember { mutableStateOf(true) }
    var askForLocationPermission by remember { mutableStateOf(false) }

    if (showLocationBanner && isCourseLocation) {
        if (askForLocationPermission) {
            AskForLocationPermission(forceAsk = true) { askForLocationPermission = false }
        }
        Banner(
            title = stringResource(R.string.low_accuracy_title),
            message = stringResource(R.string.low_accuracy_message),
            onDismiss = { showLocationBanner = false },
            leadingIcon = {
                Icon(
                    modifier = Modifier.padding(end = mediumSize),
                    painter = AppIcon.LocationOff.getPainter(),
                    contentDescription = "low accuracy"
                )
            },
            onClick = { askForLocationPermission = true }
        )
    }
}

