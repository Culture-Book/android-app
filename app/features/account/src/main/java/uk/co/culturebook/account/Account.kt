package uk.co.culturebook.account

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import uk.co.culturebook.account.profile.ProfilePicture
import uk.co.culturebook.data.flows.EventBus
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.*

@Composable
fun AccountRoute(navController: NavController) {
    Account(navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun Account(navController: NavController = NavController(LocalContext.current)) {
    Scaffold(
        topBar = { ProfilePicture(modifier = Modifier.fillMaxWidth()) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(AllAccountItems) { item ->
                    AccountItemComposable(item.icon, stringResource(item.titleId)) {
                        navController.navigate(item.route)
                    }
                }
            }

            FilledTonalButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(mediumSize),
                onClick = { EventBus.logout() })
            {
                Icon(
                    modifier = Modifier.padding(horizontal = smallSize),
                    painter = AppIcon.Logout.getPainter(), contentDescription = "Logout"
                )
                Text(stringResource(R.string.logout))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleBackAppBar(title: String, onBackTapped: () -> Unit = {}) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onBackTapped) {
                Icon(
                    painter = AppIcon.ArrowBack.getPainter(),
                    contentDescription = "Back"
                )
            }
        }
    )
}
