package uk.co.culturebook.add_new.title_type

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.co.culturebook.add_new.title_type.events.TitleAndTypeEvent
import uk.co.culturebook.add_new.title_type.states.TitleAndTypeState
import uk.co.culturebook.data.remote.interfaces.ApiResponse
import uk.co.culturebook.data.repositories.cultural.AddNewRepository
import uk.co.culturebook.ui.R

class TitleAndTypeViewModel(private val addNewRepository: AddNewRepository) : ViewModel() {
    private val _titleAndTypeState = MutableStateFlow<TitleAndTypeState>(TitleAndTypeState.Idle)
    val titleAndTypeState = _titleAndTypeState.asStateFlow()

    fun postEvent(event: TitleAndTypeEvent) {
        viewModelScope.launch {
            _titleAndTypeState.emit(TitleAndTypeState.Loading)
            when (event) {
                is TitleAndTypeEvent.Submit -> {
                    when (val response =
                        addNewRepository.getDuplicateElements(
                            event.typeData.name,
                            event.typeData.type ?: return@launch
                        )) {
                        is ApiResponse.Success.Empty -> _titleAndTypeState.emit(
                            TitleAndTypeState.Error(R.string.generic_sorry)
                        )
                        is ApiResponse.Exception -> _titleAndTypeState.emit(
                            TitleAndTypeState.Error(R.string.generic_sorry)
                        )
                        is ApiResponse.Failure -> _titleAndTypeState.emit(
                            TitleAndTypeState.Error(R.string.generic_sorry)
                        )
                        is ApiResponse.Success -> {
                            if (response.data.isEmpty()) {
                                _titleAndTypeState.emit(
                                    TitleAndTypeState.Success(event.typeData)
                                )
                            } else {
                                _titleAndTypeState.emit(TitleAndTypeState.Error.Duplicate(response.data.first()))
                            }
                        }
                    }
                }
                TitleAndTypeEvent.Idle -> _titleAndTypeState.emit(TitleAndTypeState.Idle)
                is TitleAndTypeEvent.SubmitContribution ->
                    when (val response = addNewRepository.getDuplicateContributions(
                        event.typeData.name,
                        event.typeData.type ?: return@launch
                    )) {
                        is ApiResponse.Success.Empty -> _titleAndTypeState.emit(
                            TitleAndTypeState.Error(R.string.generic_sorry)
                        )
                        is ApiResponse.Exception -> _titleAndTypeState.emit(
                            TitleAndTypeState.Error(R.string.generic_sorry)
                        )
                        is ApiResponse.Failure -> _titleAndTypeState.emit(
                            TitleAndTypeState.Error(R.string.generic_sorry)
                        )
                        is ApiResponse.Success -> {
                            if (response.data.isEmpty()) {
                                _titleAndTypeState.emit(
                                    TitleAndTypeState.Success(event.typeData)
                                )
                            } else {
                                _titleAndTypeState.emit(
                                    TitleAndTypeState.Error.DuplicateContribution(
                                        response.data.first()
                                    )
                                )
                            }
                        }
                    }
            }
        }

    }


}