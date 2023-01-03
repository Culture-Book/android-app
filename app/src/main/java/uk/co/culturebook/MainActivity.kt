package uk.co.culturebook

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import uk.co.culturebook.auth.composables.ForgotRoute
import uk.co.culturebook.auth.composables.LoginRoute
import uk.co.culturebook.auth.composables.RegistrationRoute
import uk.co.culturebook.data.PrefKey
import uk.co.culturebook.data.flows.EventBus
import uk.co.culturebook.data.logD
import uk.co.culturebook.data.models.authentication.UserSessionState
import uk.co.culturebook.data.remote_config.RemoteConfig
import uk.co.culturebook.data.remote_config.getRemoteConfig
import uk.co.culturebook.data.remove
import uk.co.culturebook.data.sharedPreferences
import uk.co.culturebook.home.composables.homeGraph
import uk.co.culturebook.nav.DeepLinks
import uk.co.culturebook.nav.Route
import uk.co.culturebook.nav.navigateTop
import uk.co.culturebook.ui.theme.AppTheme
import uk.co.culturebook.ui.theme.molecules.WebViewComposable

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavController

    private fun initializeFirebase() {
        FirebaseApp.initializeApp(this) ?: "Firebase not Initialised".logD().also { return }
        getRemoteConfig(this)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        initializeFirebase()

        setContent {
            val navController = rememberNavController().also {
                navController = it
            }

            AppEventBus(navController)

            //TODO Add a navigation bus and pass events to a bottom sheet wrapper
            AppTheme {
                Scaffold { padding ->
                    AppNavHost(navController, padding)
                }
            }
        }
    }

    @Composable
    fun AppEventBus(navController: NavController) {
        val userSessionState by EventBus.userSessionFlow.collectAsState(UserSessionState.Idle)

        DisposableEffect(userSessionState) {
            if (userSessionState is UserSessionState.LoggedOut) {
                sharedPreferences.remove(PrefKey.AccessToken)
                navController.navigateTop(Route.Login)
            }
            onDispose {}
        }
    }

    @Composable
    fun AppNavHost(navController: NavHostController, paddingValues: PaddingValues) {
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = Route.Login.route
        ) {
            homeGraph(navController)
            composable(Route.Login.route) { LoginRoute(navController) }
            composable(Route.Registration.route) { RegistrationRoute(navController) }
            composable(Route.WebView.ToS.route) {
                WebViewComposable(
                    titleId = uk.co.culturebook.ui.R.string.tos,
                    url = Firebase.remoteConfig.getString(RemoteConfig.ToSUrl.key),
                    onBack = { navController.navigateUp() }
                )
            }
            composable(Route.WebView.Privacy.route) {
                WebViewComposable(
                    titleId = uk.co.culturebook.ui.R.string.privacy,
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navController.handleDeepLink(intent)
    }
}