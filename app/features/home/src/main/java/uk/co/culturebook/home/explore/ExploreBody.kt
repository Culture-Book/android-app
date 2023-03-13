package uk.co.culturebook.home.explore

import androidx.compose.foundation.layout.*
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import uk.co.common.*
import uk.co.culturebook.data.models.cultural.SearchCriteriaState
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppIcon
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.molecules.Banner
import uk.co.culturebook.ui.theme.molecules.BannerType
import uk.co.culturebook.ui.theme.molecules.LoadingComposable

@Composable
fun ExploreBody(
    modifier: Modifier,
    exploreState: ExploreState,
    searchCriteria: SearchCriteriaState,
    postEvent: (ExploreEvent) -> Unit,
) {
    Column(modifier) {
        ShowBanners()

        when (exploreState) {
            is ExploreState.Success.ElementsReceived -> {
                ShowElements(
                    elements = exploreState.elements,
                    onElementClicked = { postEvent(ExploreEvent.GoToElementDetails(it)) },
                    onOptionsClicked = {
                        when (it) {
                            is ElementOptionsState.Block -> postEvent(ExploreEvent.BlockElement(it.id))
                            is ElementOptionsState.Hide -> postEvent(ExploreEvent.BlockElement(it.id))
                            is ElementOptionsState.Report -> postEvent(ExploreEvent.BlockElement(it.id))
                        }
                    },
                    onFavouriteClicked = { postEvent(ExploreEvent.FavouriteElement(it)) }
                ) {
                    PageInformation(
                        modifier = Modifier.fillMaxWidth(),
                        onNextPage = { searchCriteria.page = it },
                        onPreviousPage = { searchCriteria.page = it },
                        currentPage = searchCriteria.page,
                        items = exploreState.elements
                    )
                }
            }
            is ExploreState.Success.ContributionsReceived -> {
                ShowContributions(
                    contributions = exploreState.contributions,
                    onClicked = {
                        postEvent(ExploreEvent.GoToContributionDetails(it))
                    },
                    onOptionsClicked = {
                        when (it) {
                            is ElementOptionsState.Block -> postEvent(
                                ExploreEvent.BlockContribution(
                                    it.id
                                )
                            )
                            is ElementOptionsState.Hide -> postEvent(
                                ExploreEvent.BlockContribution(
                                    it.id
                                )
                            )
                            is ElementOptionsState.Report -> postEvent(
                                ExploreEvent.BlockContribution(
                                    it.id
                                )
                            )
                        }
                    },
                    onFavouriteClicked = { postEvent(ExploreEvent.FavouriteContribution(it)) }
                ) {
                    PageInformation(
                        modifier = Modifier.fillMaxWidth(),
                        onNextPage = { searchCriteria.page = it },
                        onPreviousPage = { searchCriteria.page = it },
                        currentPage = searchCriteria.page,
                        items = exploreState.contributions
                    )
                }
            }
            is ExploreState.Success.CulturesReceived -> {
                ShowCultures(
                    cultures = exploreState.cultures,
                    onClicked = {},
                    onOptionsClicked = {
                        when (it) {
                            is ElementOptionsState.Block -> postEvent(ExploreEvent.BlockCulture(it.id))
                            is ElementOptionsState.Hide -> postEvent(ExploreEvent.BlockCulture(it.id))
                            is ElementOptionsState.Report -> postEvent(ExploreEvent.BlockCulture(it.id))
                        }
                    },
                    onFavouriteClicked = { postEvent(ExploreEvent.FavouriteCulture(it)) }
                ) {
                    PageInformation(
                        modifier = Modifier.fillMaxWidth(),
                        onNextPage = { searchCriteria.page = it },
                        onPreviousPage = { searchCriteria.page = it },
                        currentPage = searchCriteria.page,
                        items = exploreState.cultures
                    )
                }
            }
            else -> LoadingComposable()
        }
    }
}

@Composable
fun ShowBanners() {
    var showLocationBanner by remember { mutableStateOf(true) }
    var askForLocationPermission by remember { mutableStateOf(false) }

    if (showLocationBanner && coarseLocationOnly) {
        if (askForLocationPermission) {
            AskForLocationPermission(forceAsk = true) { askForLocationPermission = false }
        }
        Banner(title = stringResource(R.string.low_accuracy_title),
            message = stringResource(R.string.low_accuracy_message),
            onDismiss = { showLocationBanner = false },
            leadingIcon = {
                Icon(
                    modifier = Modifier.padding(end = mediumSize),
                    painter = AppIcon.LocationOff.getPainter(),
                    contentDescription = "low accuracy"
                )
            },
            onClick = { askForLocationPermission = true })
    }

    if (showLocationBanner && !(coarseLocationOnly or fineLocationGranted)) {
        if (askForLocationPermission) {
            AskForLocationPermission(forceAsk = true) { askForLocationPermission = false }
        }
        Banner(
            bannerType = BannerType.Warning,
            title = stringResource(R.string.no_location_title),
            message = stringResource(R.string.no_location_message),
            onDismiss = { showLocationBanner = false },
            leadingIcon = {
                Icon(
                    modifier = Modifier.padding(end = mediumSize),
                    painter = AppIcon.LocationOff.getPainter(),
                    contentDescription = "low accuracy"
                )
            },
            onClick = { askForLocationPermission = true })
    }
}