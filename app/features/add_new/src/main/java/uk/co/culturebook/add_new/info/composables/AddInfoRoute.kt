package uk.co.culturebook.add_new.info.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import uk.co.culturebook.add_new.data.InfoData
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppIcon

@Composable
fun AddInfoRoute(navController: NavController, onDone: (InfoData) -> Unit) {
    AddInfoScreen {
        navController.navigateUp()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddInfoScreen(onBack: () -> Unit) {

    Scaffold(topBar = { AddInfoAppbar(onBack) }) { padding ->
        AddInfoBody(modifier = Modifier.padding(padding))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddInfoAppbar(navigateBack: () -> Unit = {}) {
    TopAppBar(modifier = Modifier.fillMaxWidth(),
        title = { Text(stringResource(R.string.add_info)) },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    painter = AppIcon.ArrowBack.getPainter(), contentDescription = "navigate back"
                )
            }
        })
}