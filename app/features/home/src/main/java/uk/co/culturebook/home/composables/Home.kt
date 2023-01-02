package uk.co.culturebook.home.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import uk.co.culturebook.home.navigation.ExploreRoute
import uk.co.culturebook.nav.Route


fun NavGraphBuilder.homeGraph(navController: NavController) {
    navigation(
        ExploreRoute.Explore.route,
        Route.Home.route
    ) {
        composable(ExploreRoute.Explore.route) {
            ExploreRoute(navController)
        }
        composable(ExploreRoute.Account.route) {
            AccountRoute(navController)
        }
        composable(ExploreRoute.Social.route) {
            SocialRoute(navController)
        }
    }
}


@Composable
@Preview
fun AppBottomAppBar(
    modifier: Modifier = Modifier,
    currentDestination: NavDestination? = null,
    destinations: List<ExploreRoute> = listOf(
        ExploreRoute.Social,
        ExploreRoute.Explore,
        ExploreRoute.Account
    ),
    onItemClicked: (ExploreRoute) -> Unit = {}
) {

    BottomAppBar(
        modifier = modifier.fillMaxWidth(),
    ) {
        destinations.forEach { homeRoute ->
            with(homeRoute) {
                val isSelected =
                    currentDestination?.hierarchy?.any { it.route == route } == true
                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onItemClicked(homeRoute) },
                    icon = {
                        val icon = if (isSelected) selectedIcon else unselectedIcon
                        Icon(icon.getPainter(), stringResource(labelId))
                    },
                    label = { Text(stringResource(labelId)) }
                )
            }

        }

    }
}

@Composable
fun SearchTopAppBar() {

}
