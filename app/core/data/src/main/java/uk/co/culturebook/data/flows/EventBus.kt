package uk.co.culturebook.data.flows

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import uk.co.culturebook.data.models.authentication.UserSessionState
import java.util.*

object EventBus {
    private val _userSessionFlow = MutableStateFlow<UserSessionState>(UserSessionState.Idle)
    private val _workerFlow = MutableStateFlow<UUID?>(null)
    val userSessionFlow = _userSessionFlow.asSharedFlow()
    val workerFlow = _workerFlow.asStateFlow()

    fun logout() {
        _userSessionFlow.value = UserSessionState.LoggedOut
    }

    fun login() {
        _userSessionFlow.value = UserSessionState.Idle
    }

    fun registerRunningWorker(id: UUID) {
        _workerFlow.value = id
    }
}