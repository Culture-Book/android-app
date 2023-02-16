package uk.co.culturebook.home.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun SocialRoute(navController: NavController) {
    Social(navController)
}

@Composable
fun Social(navController: NavController) {
    Text("Social")
}