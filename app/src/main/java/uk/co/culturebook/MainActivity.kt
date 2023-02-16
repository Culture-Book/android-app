package uk.co.culturebook

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import uk.co.culturebook.composables.App
import uk.co.culturebook.composables.ManageWorkerState
import uk.co.culturebook.data.PrefKey
import uk.co.culturebook.data.flows.EventBus
import uk.co.culturebook.data.logD
import uk.co.culturebook.data.models.authentication.UserSessionState
import uk.co.culturebook.data.remote_config.getRemoteConfig
import uk.co.culturebook.data.remove
import uk.co.culturebook.data.sharedPreferences
import uk.co.culturebook.nav.Route
import uk.co.culturebook.nav.navigateTop
import uk.co.culturebook.states.AppState
import uk.co.culturebook.states.rememberAppState

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavController

    private fun initializeFirebase() {
        FirebaseApp.initializeApp(this) ?: "Firebase not Initialised".logD().also { return }
        getRemoteConfig(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        initializeFirebase()

        setContent {
            val appState = rememberAppState()
            val navController = rememberNavController().also {
                navController = it
            }

            AppEventBus(navController, appState)

            //TODO Add a navigation bus and pass events to a bottom sheet wrapper
            App(appState, navController)
        }
    }

    @Composable
    fun AppEventBus(navController: NavController, appState: AppState) {
        val userSessionState by EventBus.userSessionFlow.collectAsState(UserSessionState.Idle)
        val currentlyRunningWorkerId by EventBus.workerFlow.collectAsState()

        ManageWorkerState(workId = currentlyRunningWorkerId, appState = appState)

        DisposableEffect(userSessionState) {
            if (userSessionState is UserSessionState.LoggedOut) {
                sharedPreferences.remove(PrefKey.AccessToken)
                navController.navigateTop(Route.Login)
            }
            onDispose {}
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navController.handleDeepLink(intent)
    }
}