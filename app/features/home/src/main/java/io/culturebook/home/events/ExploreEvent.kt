package io.culturebook.home.events

sealed interface ExploreEvent {
    object Success : ExploreEvent
    object Idle : ExploreEvent
    object GetUser : ExploreEvent
    object UpdateToS : ExploreEvent
    sealed interface Error : ExploreEvent {
        object ToSUpdate : Error
        data class Generic(val stringId: Int) : Error
    }
}