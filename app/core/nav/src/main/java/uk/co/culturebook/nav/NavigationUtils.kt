package uk.co.culturebook.nav

import androidx.navigation.NavController
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/** This method re-iterates why jetpack navigation is so shit. *sigh* If you continue like this,
 *  I'll switch to iOS Development Google... I swear...
 *
 *  Empty the back stack and then navigate. Because apparently it was too hard to implement a simple while loop. Seriously fuck my life!
 *  */
fun NavController.navigateTop(route: Route) {
    while (backQueue.size > 0) { backQueue.removeLast() }
    navigate(route.route)
}

inline fun <reified T : Any> T.toJsonString() = Json.encodeToString(this)
inline fun <reified T : Any> String.fromJsonString(): T = Json.decodeFromString(this)