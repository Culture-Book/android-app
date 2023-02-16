package uk.co.culturebook.add_new.submit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.co.culturebook.add_new.data.AddNewState
import uk.co.culturebook.data.remote.interfaces.ApiResponse
import uk.co.culturebook.data.repositories.cultural.AddNewRepository

class SubmitViewModel(
    private val addNewRepository: AddNewRepository
) : ViewModel() {
    private val _submitState = MutableStateFlow<SubmitState>(SubmitState.Idle)
    val submitState = _submitState.asStateFlow()


    fun submit(addNewState: AddNewState) {
        viewModelScope.launch {
            _submitState.emit(SubmitState.Loading)
            val state =
                when (addNewRepository.postElement(addNewState.toElement(), addNewState.files)) {
                    is ApiResponse.Success.Empty -> SubmitState.Success
                    is ApiResponse.Exception -> SubmitState.Error
                    is ApiResponse.Failure -> SubmitState.Error
                    is ApiResponse.Success -> SubmitState.Success
                }
            _submitState.emit(state)
        }
    }
}