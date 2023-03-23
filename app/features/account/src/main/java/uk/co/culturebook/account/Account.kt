package uk.co.culturebook.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import uk.co.common.rememberImageLoader
import uk.co.culturebook.data.PrefKey
import uk.co.culturebook.data.flows.EventBus
import uk.co.culturebook.data.sharedPreferences
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
        topBar = { AccountAppBar {} }
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
                    AccountItemComposable(item.icon, stringResource(item.titleId))
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

@Preview
@Composable
fun AccountAppBar(onProfileTapped: () -> Unit = {}) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .padding(smallSize)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val displayName = context.sharedPreferences.getString(PrefKey.UserDisplayName.key, null)
        val uri = context.sharedPreferences.getString(PrefKey.UserProfileUri.key, null)
        val imageLoader = rememberImageLoader(AppIcon.AccountCircle.icon)
        if (!uri.isNullOrEmpty()) {
            var isSuccess by remember { mutableStateOf(false) }
            AsyncImage(
                model = uri,
                imageLoader = imageLoader,
                contentDescription = displayName,
                contentScale = ContentScale.FillBounds,
                onState = { state ->
                    isSuccess = state is AsyncImagePainter.State.Success
                },
                modifier = Modifier
                    .size(xxxlSize, xxxlSize)
                    .padding(smallSize)
                    .clip(CircleShape)
                    .clickable { onProfileTapped() },
                colorFilter = if (isSuccess) null else ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
            )
        } else {
            Icon(
                modifier = Modifier
                    .size(xxxlSize, xxxlSize)
                    .padding(smallSize)
                    .clip(CircleShape)
                    .clickable { onProfileTapped() },
                painter = AppIcon.AccountCircle.getPainter(),
                contentDescription = displayName
            )
        }
        if (!displayName.isNullOrEmpty()) {
            Text(text = stringResource(R.string.hey_name, displayName))
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountItemComposable(icon: AppIcon, title: String, onClick: () -> Unit = {}) {
    Surface(
        modifier = Modifier
            .padding(smallSize)
            .fillMaxWidth()
            .height(xxxlSize),
        shape = mediumRoundedShape,
        tonalElevation = mediumSize,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .padding(smallSize)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(painter = icon.getPainter(), contentDescription = title)
            Text(text = title)
        }
    }
}
