package uk.co.culturebook.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import uk.co.culturebook.add_new.addNewGraph
import uk.co.culturebook.auth.composables.ForgotRoute
import uk.co.culturebook.auth.composables.LoginRoute
import uk.co.culturebook.auth.composables.RegistrationRoute
import uk.co.culturebook.data.remote_config.RemoteConfig
import uk.co.culturebook.home.composables.homeGraph
import uk.co.culturebook.nav.DeepLinks
import uk.co.culturebook.nav.Route
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.molecules.WebViewComposable


@Composable
fun AppNavHost(modifier: Modifier, navController: NavHostController) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Route.Login.route
    ) {
        homeGraph(navController)
        addNewGraph(navController)
        composable(Route.Login.route) { LoginRoute(navController) }
        composable(Route.Registration.route) { RegistrationRoute(navController) }
        composable(Route.WebView.ToS.route) {
            WebViewComposable(
                titleId = R.string.tos,
                url = Firebase.remoteConfig.getString(RemoteConfig.ToSUrl.key),
                onBack = { navController.navigateUp() }
            )
        }
        composable(Route.WebView.Privacy.route) {
            WebViewComposable(
                titleId = R.string.privacy,
                url = Firebase.remoteConfig.getString(RemoteConfig.PrivacyUrl.key),
                onBack = { navController.navigateUp() }
            )
        }
        composable(
            Route.Forgot.route,
            deepLinks = DeepLinks.forgotPasswordDeepLinks,
            arguments = Route.Forgot.arguments
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString(Route.Forgot.userIdArgument) ?: ""
            val token = backStackEntry.arguments?.getString(Route.Forgot.tokenArgument) ?: ""
            ForgotRoute(navController, userId, token)
        }
    }
}
