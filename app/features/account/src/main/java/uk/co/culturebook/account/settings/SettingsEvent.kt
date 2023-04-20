package uk.co.culturebook.account.settings

import java.util.UUID

sealed interface SettingsEvent {
    object Idle : SettingsEvent
    object DeleteAccount : SettingsEvent
    object FetchBlockedList : SettingsEvent
    data class UnblockElement(val uuid: UUID) : SettingsEvent
    data class UnblockCulture(val uuid: UUID) : SettingsEvent
    data class UnblockContribution(val uuid: UUID) : SettingsEvent
}