package uk.co.culturebook.home.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.co.culturebook.data.logD
import uk.co.culturebook.data.logE
import uk.co.culturebook.data.models.cultural.SearchCriteriaState
import uk.co.culturebook.data.remote.interfaces.ApiResponse
import uk.co.culturebook.data.remote.interfaces.getDataOrNull
import uk.co.culturebook.data.repositories.authentication.UserRepository
import uk.co.culturebook.data.repositories.cultural.NearbyRepository
import uk.co.culturebook.ui.R

class ExploreViewModel(
    private val userRepository: UserRepository,
    private val nearbyRepository: NearbyRepository
) : ViewModel() {

    private val _exploreState = MutableStateFlow<ExploreState>(ExploreState.Idle)
    val exploreState = _exploreState.asStateFlow()
    val searchCriteriaState = SearchCriteriaState()

    init {
        getUser()
    }

    fun postEvent(event: ExploreEvent) {
        viewModelScope.launch {
            when (event) {
                ExploreEvent.Idle -> _exploreState.emit(ExploreState.Idle)
                ExploreEvent.GetUser -> getUser()
                ExploreEvent.UpdateToS -> updateToS()
                is ExploreEvent.GetElements -> getElements(event.searchCriteriaState)
                ExploreEvent.Error.ToSUpdate -> _exploreState.emit(ExploreState.Error.ToSUpdate)
                is ExploreEvent.Error.Generic -> _exploreState.emit(ExploreState.Error.Generic(event.stringId))
                is ExploreEvent.GetContributions -> getContributions(event.searchCriteriaState)
                is ExploreEvent.GetCultures -> getCultures(event.searchCriteriaState)
            }
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            _exploreState.emit(ExploreState.Loading)
            when (val user = userRepository.getUser()) {
                is ApiResponse.Success, is ApiResponse.Success.Empty -> {
                    _exploreState.emit(ExploreState.Success.UserFetched)
                }
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
                is ApiResponse.Success, is ApiResponse.Success.Empty -> postEvent(ExploreEvent.GetUser)
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

    private fun getElements(searchCriteriaState: SearchCriteriaState) {
        viewModelScope.launch {
            val response = nearbyRepository.getElements(searchCriteriaState.toSearchCriteria())
            when (response) {
                is ApiResponse.Success -> {
                    val elements = response.data.map { element ->
                        val media = nearbyRepository.getElementMedia(element.id)?.getDataOrNull()
                            ?: emptyList()
                        element.copy(media = media)
                    }
                    _exploreState.emit(ExploreState.Success.ElementsReceived(elements))
                }
                else -> _exploreState.emit(ExploreState.Error.Generic(R.string.generic_sorry))
            }
        }
    }

    private fun getContributions(searchCriteriaState: SearchCriteriaState) {
        viewModelScope.launch {
            val response = nearbyRepository.getContributions(searchCriteriaState.toSearchCriteria())
            when (response) {
                is ApiResponse.Success -> {
                    val contributions = response.data.map { contribution ->
                        val media =
                            nearbyRepository.getContributionMedia(contribution.id)?.getDataOrNull()
                                ?: emptyList()
                        contribution.copy(media = media)
                    }
                    _exploreState.emit(ExploreState.Success.ContributionsReceived(contributions))
                }
                else -> _exploreState.emit(ExploreState.Error.Generic(R.string.generic_sorry))
            }
        }
    }

    private fun getCultures(searchCriteriaState: SearchCriteriaState) {
        viewModelScope.launch {
            val response = nearbyRepository.getCultures(searchCriteriaState.toSearchCriteria())
            when (response) {
                is ApiResponse.Success -> {
                    _exploreState.emit(ExploreState.Success.CulturesReceived(response.data))
                }
                else -> _exploreState.emit(ExploreState.Error.Generic(R.string.generic_sorry))
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