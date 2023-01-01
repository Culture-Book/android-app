package io.culturebook

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
import io.culturebook.auth.composables.LoginRoute
import io.culturebook.auth.composables.RegistrationRoute
import io.culturebook.data.PrefKey
import io.culturebook.data.flows.EventBus
import io.culturebook.data.logD
import io.culturebook.data.models.authentication.UserSessionState
import io.culturebook.data.remote_config.RemoteConfig
import io.culturebook.data.remote_config.getRemoteConfig
import io.culturebook.data.remove
import io.culturebook.data.sharedPreferences
import io.culturebook.nav.Route
import io.culturebook.nav.navigateTop
import io.culturebook.nearby.composables.NearbyRoute
import io.culturebook.ui.theme.AppTheme
import io.culturebook.ui.theme.molecules.WebViewComposable

class MainActivity : ComponentActivity() {

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
            val navController = rememberNavController()

            AppEventBus(navController)

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
            composable(Route.Login.route) { LoginRoute(navController) }
            composable(Route.Registration.route) { RegistrationRoute(navController) }
            composable(Route.Nearby.route) { NearbyRoute() }
            composable(Route.WebView.ToS.route) {
                WebViewComposable(
                    titleId = io.culturebook.ui.R.string.tos,
                    url = Firebase.remoteConfig.getString(RemoteConfig.ToSUrl.key),
                    onBack = { navController.navigateUp() }
                )
            }
            composable(Route.WebView.Privacy.route) {
                WebViewComposable(
                    titleId = io.culturebook.ui.R.string.privacy,
                    url = Firebase.remoteConfig.getString(RemoteConfig.PrivacyUrl.key),
                    onBack = { navController.navigateUp() }
                )
            }
        }
    }

}