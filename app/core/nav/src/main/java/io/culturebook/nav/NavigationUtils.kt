package io.culturebook.nav

import androidx.navigation.NavController

fun NavController.navigateTop(route: Route) {
    navigate(route.route) {
        launchSingleTop = true
        popUpTo(currentBackStackEntry?.destination?.route ?: return@navigate) {
            inclusive = true
        }
    }
}