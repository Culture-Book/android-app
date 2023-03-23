package uk.co.culturebook.account.settings

import androidx.annotation.StringRes
import uk.co.culturebook.data.models.cultural.BlockedList
import uk.co.culturebook.ui.R

sealed interface SettingsState {
    object Idle : SettingsState
    object Loading : SettingsState
    object AccountDeleted : SettingsState
    object ElementUnblocked : SettingsState
    data class BlockedListFetched(val blockedList: BlockedList) : SettingsState
    data class Error(@StringRes val messageId: Int = R.string.generic_sorry) : SettingsState
}