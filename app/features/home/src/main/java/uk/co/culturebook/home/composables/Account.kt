package uk.co.culturebook.home.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun AccountRoute(navController: NavController) {
    Account(navController)
}

@Composable
fun Account(navController: NavController) {
    Text("ACCOUNT")
}