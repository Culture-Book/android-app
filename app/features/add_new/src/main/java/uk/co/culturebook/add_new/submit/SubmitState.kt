package uk.co.culturebook.add_new.submit

sealed interface SubmitState {
    object Idle : SubmitState
    object Loading : SubmitState
    object Success : SubmitState
    object Error : SubmitState
}