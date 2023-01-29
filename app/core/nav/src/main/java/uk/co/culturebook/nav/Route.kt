package uk.co.culturebook.nav

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed interface Route {
    val route: String

    object Registration : Route {
        override val route = "registration"
    }

    object Login : Route {
        override val route = "login"
    }

    object Forgot : Route {
        const val userIdArgument = "userId"
        const val tokenArgument = "token"
        override val route = "forgot"
        val arguments = listOf(
            navArgument(userIdArgument) {
                defaultValue = ""
                type = NavType.StringType
            },
            navArgument(tokenArgument) {
                defaultValue = ""
                type = NavType.StringType
            }
        )
    }

    object Home : Route {
        override val route = "home"
    }

    object AddNew : Route {
        override val route = "add_new"

        object Location : Route {
            override val route = "location"
        }

        object TitleAndType : Route {
            override val route = "title_type"
        }

        object AddInfo : Route {
            override val route = "add_info"
        }

    }

    object WebView : Route {
        override val route = "webview"

        object ToS : Route {
            override val route = "webview/tos"
        }

        object Privacy : Route {
            override val route = "webview/privacy"
        }
    }
}