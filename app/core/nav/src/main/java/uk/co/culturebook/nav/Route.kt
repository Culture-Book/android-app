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

    object Explore : Route {
        override val route = "explore"
    }

    object Account : Route {
        override val route = "account"

        object Home : Route {
            override val route = "${Route.Account.route}/home"
        }

        object Favourites : Route {
            override val route = "${Route.Account.route}/favourites"
        }

        object Profile : Route {
            override val route = "${Route.Account.route}/profile"
        }

        object Elements : Route {
            override val route = "${Route.Account.route}/elements"
        }

        object About : Route {
            override val route = "${Route.Account.route}/about"
        }

        object Settings : Route {
            override val route = "${Route.Account.route}/settings"
        }
    }

    object Details : Route {
        const val id = "id"
        const val isContribution = "is_contribution"
        override val route = "details"

        object ShowContributions : Route {
            const val elementId = "element_id"
            override val route = "show_contributions"
        }

        val args = listOf(
            navArgument(id) { defaultValue = "" },
            navArgument(isContribution) {
                defaultValue = false
                type = NavType.BoolType
            }
        )
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

            object Base : Route {
                override val route = "base"
            }

            object LinkElements : Route {
                override val route = "link_elements"
            }
        }

        object Review : Route {
            override val route = "review"
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