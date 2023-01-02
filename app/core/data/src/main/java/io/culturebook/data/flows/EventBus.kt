package io.culturebook.data.flows

import io.culturebook.data.models.authentication.UserSessionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.runBlocking

object EventBus {
    private val _userSessionFlow = MutableStateFlow<UserSessionState>(UserSessionState.Idle)
    val userSessionFlow = _userSessionFlow.asSharedFlow()

    fun logout() {
        runBlocking(Dispatchers.IO) {
            _userSessionFlow.emit(UserSessionState.LoggedOut)
        }
    }
}