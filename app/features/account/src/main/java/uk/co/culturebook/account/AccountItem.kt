package uk.co.culturebook.account

import androidx.annotation.StringRes
import uk.co.culturebook.ui.theme.AppIcon
import uk.co.culturebook.ui.R

sealed interface AccountItem {
    val icon: AppIcon
    @get:StringRes val titleId: Int

    data class Profile(override val icon: AppIcon, @StringRes override val titleId: Int): AccountItem
    data class Favourites(override val icon: AppIcon, @StringRes override val titleId: Int): AccountItem
    data class Settings(override val icon: AppIcon, @StringRes override val titleId: Int): AccountItem
    data class About(override val icon: AppIcon, @StringRes override val titleId: Int): AccountItem
    data class Elements(override val icon: AppIcon, @StringRes override val titleId: Int): AccountItem
}

val AllAccountItems: List<AccountItem>
    get() = listOf(
        AccountItem.Profile(AppIcon.Account, R.string.profile),
        AccountItem.Favourites(AppIcon.FavouriteFilled, R.string.favourites),
        AccountItem.Settings(AppIcon.Settings, R.string.settings),
        AccountItem.Elements(AppIcon.Castle, R.string.elements),
        AccountItem.About(AppIcon.About, R.string.about),
    )