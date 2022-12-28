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
import io.culturebook.auth.composables.LoginRoute
import io.culturebook.auth.composables.RegistrationRoute
import io.culturebook.nav.Route
import io.culturebook.nearby.composables.NearbyRoute
import io.culturebook.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                Scaffold { padding ->
                    val navController = rememberNavController()
                    NavHost(
                        modifier = Modifier.padding(padding),
                        navController = navController, startDestination = Route.Login.route
                    ) {
                        composable(Route.Login.route) { LoginRoute(navController) }
                        composable(Route.Registration.route) { RegistrationRoute() }
                        composable(Route.Nearby.route) { NearbyRoute() }
                    }
                }
            }
        }
    }
}