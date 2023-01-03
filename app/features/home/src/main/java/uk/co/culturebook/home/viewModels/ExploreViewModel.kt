package uk.co.culturebook.home.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.co.culturebook.data.logD
import uk.co.culturebook.data.logE
import uk.co.culturebook.data.remote.interfaces.ApiResponse
import uk.co.culturebook.data.repositories.authentication.UserRepository
import uk.co.culturebook.home.events.ExploreEvent
import uk.co.culturebook.home.states.ExploreState
import uk.co.culturebook.ui.R

class ExploreViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _exploreState = MutableStateFlow<ExploreState>(ExploreState.Idle)
    val exploreState = _exploreState.asStateFlow()

    fun postEvent(event: ExploreEvent) {
        viewModelScope.launch {
            when (event) {
                ExploreEvent.Idle -> _exploreState.emit(ExploreState.Idle)
                ExploreEvent.Success -> _exploreState.emit(ExploreState.Success)
                ExploreEvent.GetUser -> getUser()
                ExploreEvent.UpdateToS -> updateToS()
                ExploreEvent.Error.ToSUpdate -> _exploreState.emit(ExploreState.Error.ToSUpdate)
                is ExploreEvent.Error.Generic -> _exploreState.emit(ExploreState.Error.Generic(event.stringId))
            }
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            _exploreState.emit(ExploreState.Loading)
            when (val user = userRepository.getUser()) {
                is ApiResponse.Success -> postEvent(ExploreEvent.Success)
                is ApiResponse.Failure -> {
                    when (user.message) {
                        "ToSUpdate", "PrivacyUpdate" -> postEvent(ExploreEvent.Error.ToSUpdate)
                        else -> postEvent(ExploreEvent.Error.Generic(user.errorMessage))
                    }
                }
                is ApiResponse.Exception -> user.throwable.logE()
            }
        }
    }

    private fun updateToS() {
        viewModelScope.launch {
            _exploreState.emit(ExploreState.Loading)
            when (val apiResponse = userRepository.updateTos()) {
                is ApiResponse.Success -> postEvent(ExploreEvent.Success)
                is ApiResponse.Failure -> {
                    postEvent(ExploreEvent.Error.Generic(R.string.generic_sorry))
                    apiResponse.message.logD()
                }
                is ApiResponse.Exception -> {
                    postEvent(ExploreEvent.Error.Generic(R.string.generic_sorry))
                    apiResponse.throwable.logE()
                }
            }
        }
    }

    private val <T : Any> ApiResponse.Failure<T>.errorMessage
        get() = when (message) {
            "ToSUpdate", "PrivacyUpdate" -> R.string.tos_update
            else -> R.string.generic_sorry
        }
    private val <T : Any> ApiResponse.Exception<T>.errorMessage: Int
        get() {
            throwable.message.logD().also { return R.string.generic_sorry }
        }
}