package uk.co.culturebook.account.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.co.culturebook.data.models.cultural.BlockedList
import uk.co.culturebook.data.remote.interfaces.ApiResponse
import uk.co.culturebook.data.remote.interfaces.getDataOrNull
import uk.co.culturebook.data.repositories.authentication.UserRepository
import uk.co.culturebook.data.repositories.cultural.UpdateRepository
import java.util.*

class SettingsViewModel(
    private val userRepository: UserRepository,
    private val updateRepository: UpdateRepository
) : ViewModel() {
    private val _state = MutableStateFlow<SettingsState>(SettingsState.Idle)
    val state get() = _state.asStateFlow()

    fun postEvent(event: SettingsEvent) {
        updateState(SettingsState.Loading)
        viewModelScope.launch {
            when (event) {
                is SettingsEvent.DeleteAccount -> deleteUser()
                is SettingsEvent.UnblockContribution -> unblockContribution(event.uuid)
                is SettingsEvent.UnblockCulture -> unblockCulture(event.uuid)
                is SettingsEvent.UnblockElement -> unblockElement(event.uuid)
                SettingsEvent.FetchBlockedList -> fetchBlockedList()
                SettingsEvent.Idle -> updateState(SettingsState.Idle)
            }
        }
    }

    private suspend fun deleteUser() {
        when (userRepository.deleteAccount()) {
            is ApiResponse.Success.Empty,
            is ApiResponse.Success -> {
                updateState(SettingsState.AccountDeleted)
            }
            is ApiResponse.Exception,
            is ApiResponse.Failure -> {
                updateState(SettingsState.Error())
            }
        }
    }

    private suspend fun unblockElement(uuid: UUID) {
        when (updateRepository.unblockElement(uuid)) {
            is ApiResponse.Success.Empty,
            is ApiResponse.Success -> {
                updateState(SettingsState.ElementUnblocked)
                postEvent(SettingsEvent.FetchBlockedList)
            }
            is ApiResponse.Exception,
            is ApiResponse.Failure -> {
                updateState(SettingsState.Error())
            }
        }
    }

    private suspend fun unblockCulture(uuid: UUID) {
        when (updateRepository.unblockCulture(uuid)) {
            is ApiResponse.Success.Empty,
            is ApiResponse.Success -> {
                updateState(SettingsState.ElementUnblocked)
                postEvent(SettingsEvent.FetchBlockedList)
            }
            is ApiResponse.Exception,
            is ApiResponse.Failure -> {
                updateState(SettingsState.Error())
            }
        }
    }

    private suspend fun unblockContribution(uuid: UUID) {
        when (updateRepository.unblockContribution(uuid)) {
            is ApiResponse.Success.Empty,
            is ApiResponse.Success -> {
                updateState(SettingsState.ElementUnblocked)
                postEvent(SettingsEvent.FetchBlockedList)
            }
            is ApiResponse.Exception,
            is ApiResponse.Failure -> {
                updateState(SettingsState.Error())
            }
        }
    }

    private suspend fun fetchBlockedList() {
        when (val response = updateRepository.getBlockedList()) {
            is ApiResponse.Success.Empty,
            is ApiResponse.Success -> {
                updateState(
                    SettingsState.BlockedListFetched(
                        response.getDataOrNull() ?: BlockedList()
                    )
                )
            }
            is ApiResponse.Exception,
            is ApiResponse.Failure -> {
                updateState(SettingsState.Error())
            }
        }
    }

    private fun updateState(state: SettingsState) {
        _state.value = state
    }
}