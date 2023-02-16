package uk.co.culturebook.nav

import android.content.Intent
import androidx.navigation.navDeepLink

object DeepLinks {
    private const val deepLinkUri = "api.culturebook.co.uk"

    val forgotPasswordDeepLinks = listOf(
        navDeepLink {
            uriPattern =
                "https://$deepLinkUri/forgot/{${Route.Forgot.userIdArgument}}/{${Route.Forgot.tokenArgument}}"
            action = Intent.ACTION_VIEW
        },
        navDeepLink {
            uriPattern =
                "app://$deepLinkUri/forgot/{${Route.Forgot.userIdArgument}}/{${Route.Forgot.tokenArgument}}"
            action = Intent.ACTION_VIEW
        }
    )
}