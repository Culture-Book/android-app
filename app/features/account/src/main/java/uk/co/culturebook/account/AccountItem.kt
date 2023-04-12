package uk.co.culturebook.account

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import uk.co.culturebook.nav.Route
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.*

sealed interface AccountItem {
    val icon: AppIcon

    @get:StringRes
    val titleId: Int
    val route: String

    data class Profile(
        override val icon: AppIcon,
        @StringRes override val titleId: Int,
        override val route: String
    ) : AccountItem

    data class Settings(
        override val icon: AppIcon,
        @StringRes override val titleId: Int,
        override val route: String
    ) : AccountItem

    data class About(
        override val icon: AppIcon,
        @StringRes override val titleId: Int,
        override val route: String
    ) : AccountItem

    data class Elements(
        override val icon: AppIcon,
        @StringRes override val titleId: Int,
        override val route: String
    ) : AccountItem
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

val AllAccountItems: List<AccountItem>
    get() = listOf(
        AccountItem.Profile(AppIcon.Account, R.string.profile, Route.Account.Profile.route),
        AccountItem.Settings(AppIcon.Settings, R.string.settings, Route.Account.Settings.route),
        AccountItem.Elements(AppIcon.Vase, R.string.elements, Route.Account.Elements.route),
        AccountItem.About(AppIcon.About, R.string.about, Route.Account.About.route),
    )