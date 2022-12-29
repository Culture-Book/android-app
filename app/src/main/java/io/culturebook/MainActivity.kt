package io.culturebook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import io.culturebook.auth.composables.LoginRoute
import io.culturebook.auth.composables.RegistrationRoute
import io.culturebook.data.logD
import io.culturebook.data.remote_config.RemoteConfig
import io.culturebook.data.remote_config.getRemoteConfig
import io.culturebook.nav.Route
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
        super.onCreate(savedInstanceState)

        initializeFirebase()

        setContent {
            AppTheme {
                Scaffold { padding ->
                    val navController = rememberNavController()
                    NavHost(
                        modifier = Modifier.padding(padding),
                        navController = navController, startDestination = Route.Login.route
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
        }
    }
}