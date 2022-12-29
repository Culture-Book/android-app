package io.culturebook.nav

sealed interface Route {
    val route: String

    object Registration : Route {
        override val route = "registration"
    }

    object Login : Route {
        override val route = "login"
    }

    object Nearby : Route {
        override val route = "nearby"
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