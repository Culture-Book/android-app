package io.culturebook.data.models.authentication

sealed interface UserSessionState {
    object Idle : UserSessionState
    object LoggedOut : UserSessionState
}