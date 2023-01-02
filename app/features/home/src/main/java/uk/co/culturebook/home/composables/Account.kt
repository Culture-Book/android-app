package uk.co.culturebook.home.composables

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun AccountRoute(navController: NavController) {
    Account(navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Account(navController: NavController) {
    Scaffold(
        bottomBar = {
            AppBottomAppBar(
                currentDestination = navController.currentDestination,
                onItemClicked = { navController.navigate(it.route) })
        }
    ) { paddingValues ->
        Text("ACCOUNT")
    }
}