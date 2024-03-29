package uk.co.culturebook.account.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import uk.co.culturebook.common.rememberImageLoader
import uk.co.culturebook.data.PrefKey
import uk.co.culturebook.data.models.authentication.User
import uk.co.culturebook.data.sharedPreferences
import uk.co.culturebook.data.utils.toUri
import uk.co.culturebook.nav.fromJsonString
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppIcon
import uk.co.culturebook.ui.theme.smallSize
import uk.co.culturebook.ui.theme.xxxlSize


@Preview
@Composable
fun ProfilePicture(modifier: Modifier = Modifier, onProfileTapped: () -> Unit = {}) {
    val context = LocalContext.current
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val user = remember {
            context.sharedPreferences.getString(PrefKey.User.key, null)?.fromJsonString<User>()
        }
        val displayName = user?.displayName
        val uri = user?.profileUri?.toUri()
        val imageLoader = rememberImageLoader()
        if (!uri?.toString().isNullOrEmpty()) {
            AsyncImage(
                model = uri,
                imageLoader = imageLoader,
                contentDescription = displayName,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(xxxlSize, xxxlSize)
                    .padding(smallSize)
                    .clip(CircleShape)
                    .clickable { onProfileTapped() },
                colorFilter = null
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
