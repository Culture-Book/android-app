package uk.co.culturebook.account.about

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import uk.co.culturebook.account.SimpleBackAppBar
import uk.co.culturebook.nav.Route
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.molecules.LargeDynamicRoundedTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutRoute(navController: NavController) {
    Scaffold(
        topBar = {
            SimpleBackAppBar(
                title = stringResource(id = R.string.about),
                onBackTapped = { navController.navigateUp() })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = mediumSize)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = mediumSize),
                text = stringResource(R.string.about_string),
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = mediumSize),
                onClick = { navController.navigate(Route.WebView.ToS.route) }) {
                Text(text = stringResource(id = R.string.tos))
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { navController.navigate(Route.WebView.Privacy.route) }) {
                Text(text = stringResource(id = R.string.privacy))
            }

        }
    }
}