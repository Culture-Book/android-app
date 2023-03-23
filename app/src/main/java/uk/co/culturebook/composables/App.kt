package uk.co.culturebook.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import uk.co.culturebook.explore.ExploreRoute
import uk.co.culturebook.states.AppState
import uk.co.culturebook.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(appState: AppState, navController: NavHostController) {
    val currentEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentEntry?.destination?.route ?: ""

    AppTheme {
        Scaffold(
            bottomBar = {
                AppBottomAppBar(
                    currentDestination = currentDestination,
                    onItemClicked = { navController.navigate(it.route) }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier.padding(padding)
            ) {
                ShowBannerMessage(appState = appState)
                AppNavHost(modifier = Modifier.fillMaxSize(), navController)
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun AppBottomAppBar(
    modifier: Modifier = Modifier,
    currentDestination: String = "social",
    destinations: List<ExploreRoute> = listOf(
        ExploreRoute.Explore,
        ExploreRoute.Account
    ),
    onItemClicked: (ExploreRoute) -> Unit = {}
) {
    val topLevelRoutes by remember { derivedStateOf { destinations.map { it.route } } }

    if (!topLevelRoutes.contains(currentDestination)) {
        SystemBarsColors(navigationColor = MaterialTheme.colorScheme.background)
        return
    }

    val containerColor = surfaceColorAtElevation(
        BottomAppBarDefaults.containerColor,
        BottomAppBarDefaults.ContainerElevation
    )
    SystemBarsColors(navigationColor = containerColor)

    BottomAppBar(
        modifier = modifier.fillMaxWidth(),
    ) {
        destinations.forEach { homeRoute ->
            with(homeRoute) {
                val isSelected = currentDestination == route
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