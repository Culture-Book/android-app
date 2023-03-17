package uk.co.culturebook.nav

import androidx.navigation.navDeepLink
import java.util.*

object DeepLinks {
    private const val deepLinkUri = "api.culturebook.co.uk"

    val forgotPasswordDeepLinks = listOf(
        navDeepLink {
            uriPattern =
                "https://$deepLinkUri/forgot/{${Route.Forgot.userIdArgument}}/{${Route.Forgot.tokenArgument}}"
        },
        navDeepLink {
            uriPattern =
                "app://$deepLinkUri/forgot/{${Route.Forgot.userIdArgument}}/{${Route.Forgot.tokenArgument}}"
        }
    )

    val detailsDeepLink = listOf(
        navDeepLink {
            uriPattern =
                "https://$deepLinkUri/" + Route.Details.route + "?" + Route.Details.id + "=" +
                        "{${Route.Details.id}}" + "&" + Route.Details.isContribution + "=" +
                        "{${Route.Details.isContribution}}"
        },
        navDeepLink {
            uriPattern =
                "app://$deepLinkUri/" + Route.Details.route + "?" + Route.Details.id + "=" +
                        "{${Route.Details.id}}" + "&" + Route.Details.isContribution + "=" +
                        "{${Route.Details.isContribution}}"
        }
    )

    fun buildDetailsDeepLink(id: UUID, isContribution: Boolean) =
        "app://$deepLinkUri/${Route.Details.route}?${Route.Details.id}=$id&${Route.Details.isContribution}=$isContribution"
}