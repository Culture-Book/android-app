package uk.co.culturebook.nav

import android.content.Intent
import androidx.navigation.navDeepLink

object DeepLinks {
    const val deepLinkUri = "culturebook.co.uk"

    val forgotPasswordDeepLinks = listOf(
        navDeepLink {
            uriPattern =
                "https://$deepLinkUri/{${Route.Forgot.userIdArgument}}/{${Route.Forgot.tokenArgument}}"
            action = Intent.ACTION_VIEW

        },
        navDeepLink {
            uriPattern =
                "app://$deepLinkUri/{${Route.Forgot.userIdArgument}}/{${Route.Forgot.tokenArgument}}"
            action = Intent.ACTION_VIEW
        }
    )
}