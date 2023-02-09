package uk.co.culturebook.nav

import androidx.navigation.NavController
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun NavController.navigateTop(route: Route) {
    navigate(route.route) {
        launchSingleTop = true
        popUpTo(currentBackStackEntry?.destination?.route ?: return@navigate) {
            inclusive = true
        }
    }
}

inline fun <reified T : Any> T.toJsonString() = Json.encodeToString(this)
inline fun <reified T : Any> String.fromJsonString(): T = Json.decodeFromString(this)