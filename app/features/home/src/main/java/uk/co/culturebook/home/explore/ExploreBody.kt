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
import java.util.*

@Composable
fun ExploreBody(
    modifier: Modifier,
    exploreState: ExploreState,
    searchCriteria: SearchCriteriaState,
    postEvent: (ExploreEvent) -> Unit
) {
    var maxPage by remember { mutableStateOf(searchCriteria.page) }

    LaunchedEffect(exploreState) {
        maxPage = when (exploreState) {
            is ExploreState.Success.ElementsReceived ->
                if (exploreState.elements.size < 3) searchCriteria.page else maxPage + 1
            else -> 1
        }
    }

    Column(modifier) {
        ShowBanners()

        when (exploreState) {
            is ExploreState.Success.ElementsReceived -> {
                ShowElements(
                    elements = exploreState.elements,
                    onElementClicked = {},
                    onOptionsClicked = {
                        when (it) {
                            is ElementOptionsState.Block -> postEvent(ExploreEvent.BlockElement(it.id))
                            is ElementOptionsState.Hide -> postEvent(ExploreEvent.BlockElement(it.id))
                            is ElementOptionsState.Report -> postEvent(ExploreEvent.BlockElement(it.id))
                        }
                    },
                    onShowNearby = { searchCriteria.searchString = "" }
                ) {
                    PageInformation(
                        modifier = Modifier.fillMaxWidth(),
                        onNextPage = { searchCriteria.page = it },
                        onPreviousPage = { searchCriteria.page = it },
                        currentPage = searchCriteria.page,
                        maxPage = maxPage
                    )
                }
            }
            is ExploreState.Success.ContributionsReceived -> {
                ShowContributions(
                    contributions = exploreState.contributions,
                    onClicked = {},
                    onOptionsClicked = {
                        when (it) {
                            is ElementOptionsState.Block -> postEvent(ExploreEvent.BlockContribution(it.id))
                            is ElementOptionsState.Hide -> postEvent(ExploreEvent.BlockContribution(it.id))
                            is ElementOptionsState.Report -> postEvent(ExploreEvent.BlockContribution(it.id))
                        }
                    },
                    onShowNearby = { searchCriteria.searchString = "" }
                ) {
                    PageInformation(
                        modifier = Modifier.fillMaxWidth(),
                        onNextPage = { searchCriteria.page = it },
                        onPreviousPage = { searchCriteria.page = it },
                        currentPage = searchCriteria.page,
                        maxPage = maxPage
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
                    onShowNearby = { searchCriteria.searchString = "" }
                ) {
                    PageInformation(
                        modifier = Modifier.fillMaxWidth(),
                        onNextPage = { searchCriteria.page = it },
                        onPreviousPage = { searchCriteria.page = it },
                        currentPage = searchCriteria.page,
                        maxPage = maxPage
                    )
                }
            }
            else -> {}
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

@Composable
fun PageInformation(
    modifier: Modifier,
    currentPage: Int = 1,
    onNextPage: (Int) -> Unit,
    onPreviousPage: (Int) -> Unit,
    minPage: Int = 1,
    maxPage: Int
) {
    if (minPage == maxPage) return

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (minPage < currentPage) {
            FilledTonalIconButton(
                onClick = {
                    val prevPage = (currentPage - 1).coerceAtLeast(minPage)
                    onPreviousPage(prevPage)
                }) {
                Icon(
                    painter = AppIcon.ChevronLeft.getPainter(), contentDescription = "previous page"
                )
            }
        }

        if (maxPage > currentPage) {
            FilledTonalIconButton(onClick = {
                val nextPage = (currentPage + 1).coerceAtMost(maxPage)
                onNextPage(nextPage)
            }) {
                Icon(painter = AppIcon.ChevronRight.getPainter(), contentDescription = "next page")
            }
        }
    }
}