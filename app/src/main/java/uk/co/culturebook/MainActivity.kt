package uk.co.culturebook

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.emoji2.bundled.BundledEmojiCompatConfig
import androidx.emoji2.text.EmojiCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.launch
import uk.co.culturebook.composables.App
import uk.co.culturebook.composables.app_state_handlers.HandleWorkerState
import uk.co.culturebook.data.PrefKey
import uk.co.culturebook.data.Singletons.getImageLoader
import uk.co.culturebook.data.Singletons.releaseVideoCache
import uk.co.culturebook.data.flows.EventBus
import uk.co.culturebook.data.logD
import uk.co.culturebook.data.models.authentication.UserSessionState
import uk.co.culturebook.data.put
import uk.co.culturebook.data.remote_config.getRemoteConfig
import uk.co.culturebook.data.sharedPreferences
import uk.co.culturebook.nav.Route
import uk.co.culturebook.nav.navigateTop
import uk.co.culturebook.states.AppState
import uk.co.culturebook.states.rememberAppState
import uk.co.culturebook.ui.theme.AppIcon
import uk.co.culturebook.ui.theme.molecules.LoadingComposable

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private var loggingOut by mutableStateOf(false)

    private fun initializeFirebase() {
        FirebaseApp.initializeApp(this) ?: "Firebase not Initialised".logD().also { return }
        getRemoteConfig(this)
    }

    private fun initializeEmojis() {
        EmojiCompat.init(BundledEmojiCompatConfig(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        initializeFirebase()
        initializeEmojis()

        setContent {
            val appState = rememberAppState()
            val navController = rememberNavController().also {
                navController = it
            }

            AppEventBus(navController, appState)

            if (loggingOut) {
                LoadingComposable()
            } else {
                App(appState, navController)
            }
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    @Composable
    fun AppEventBus(navController: NavController, appState: AppState) {
        val userSessionState by EventBus.userSessionFlow.collectAsState(UserSessionState.Idle)
        val currentlyRunningWorkerId by EventBus.workerFlow.collectAsState()
        val materialYou by EventBus.materialYouFlow.collectAsState()

        HandleWorkerState(workId = currentlyRunningWorkerId, appState = appState)

        LaunchedEffect(materialYou) {
            materialYou?.let { materialYou ->
                appState.materialYou = materialYou
                sharedPreferences.put(PrefKey.MaterialYou, materialYou)
            }
        }

        DisposableEffect(userSessionState) {
            if (userSessionState is UserSessionState.LoggedOut) {
                // Clear all data on logout
                sharedPreferences.edit().clear().apply()
                lifecycleScope.launch {
                    releaseVideoCache()
                    getImageLoader(this@MainActivity, AppIcon.ImageErrors.listOfPlaceholders).let {
                        it.diskCache?.clear()
                        it.memoryCache?.clear()
                    }

                    navController.navigateTop(Route.Login)
                    EventBus.login() // Reset the state after we are done with logging out
                }
            }
            onDispose {
                loggingOut = false
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navController.handleDeepLink(intent)
    }
}