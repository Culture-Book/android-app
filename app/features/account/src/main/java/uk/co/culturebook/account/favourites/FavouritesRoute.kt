package uk.co.culturebook.account.favourites

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import uk.co.culturebook.account.SimpleBackAppBar
import uk.co.culturebook.ui.R

@Composable
fun FavouritesRoute(navController: NavController) {
    val viewModel = viewModel {
        val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
        FavouritesViewModel()
    }

    Scaffold(
        topBar = { SimpleBackAppBar(title = stringResource(id = R.string.favourites), onBackTapped = { navController.navigateUp() }) }
    ) { padding ->
        Text(text = "Favourites", modifier = Modifier.padding(padding).clickable { navController.popBackStack() })
    }
}