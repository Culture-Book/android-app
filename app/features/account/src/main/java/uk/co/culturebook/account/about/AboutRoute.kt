package uk.co.culturebook.account.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import uk.co.culturebook.account.SimpleBackAppBar
import uk.co.culturebook.common.getVersionName
import uk.co.culturebook.nav.Route
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppIcon
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.molecules.TitleAndSubtitle
import uk.co.culturebook.ui.theme.xxxxlSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutRoute(navigate: (String) -> Unit, navigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            SimpleBackAppBar(
                title = stringResource(id = R.string.about),
                onBackTapped = navigateBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = mediumSize)
                .verticalScroll(rememberScrollState())
        ) {
            TitleAndSubtitle(
                title = stringResource(id = R.string.app_name),
                message = stringResource(id = R.string.app_slogan)
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = mediumSize),
                text = stringResource(R.string.about_string),
            )

            Icon(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(xxxxlSize)
                    .padding(bottom = mediumSize),
                painter = AppIcon.Culture3.getPainter(),
                contentDescription = "Culture Icon 3",
                tint = Color.Unspecified
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = mediumSize),
                onClick = { navigate(Route.WebView.ToS.route) }) {
                Text(text = stringResource(id = R.string.tos))
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { navigate(Route.WebView.Privacy.route) }) {
                Text(text = stringResource(id = R.string.privacy))
            }

            LocalContext.current.getVersionName()?.let {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = mediumSize),
                    text = stringResource(id = R.string.version_name, it)
                )
            }
        }
    }
}