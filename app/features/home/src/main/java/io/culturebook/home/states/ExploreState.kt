package io.culturebook.home.states

sealed interface ExploreState {
    object Success : ExploreState
    object Idle : ExploreState
    object Loading : ExploreState
    sealed interface Error : ExploreState {
        object ToSUpdate : Error
        data class Generic(val stringId: Int) : Error
    }
}