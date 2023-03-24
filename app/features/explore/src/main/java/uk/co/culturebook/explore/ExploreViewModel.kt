package uk.co.culturebook.explore

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
import uk.co.culturebook.data.repositories.cultural.ElementsRepository
import uk.co.culturebook.data.repositories.cultural.UpdateRepository
import uk.co.culturebook.ui.R
import java.util.*

class ExploreViewModel(
    private val userRepository: UserRepository,
    private val updateRepository: UpdateRepository,
    private val elementsRepository: ElementsRepository
) : ViewModel() {

    private val _exploreState = MutableStateFlow<ExploreState>(ExploreState.Idle)
    val exploreState = _exploreState.asStateFlow()
    val searchCriteriaState = SearchCriteriaState()

    fun postEvent(event: ExploreEvent) {
        viewModelScope.launch {
            _exploreState.emit(ExploreState.Loading)
            when (event) {
                ExploreEvent.Idle -> _exploreState.emit(ExploreState.Idle)
                ExploreEvent.GetElements -> getElements(searchCriteriaState)
                ExploreEvent.GetContributions -> getContributions(searchCriteriaState)
                ExploreEvent.GetCultures -> getCultures(searchCriteriaState)
                ExploreEvent.GetUser -> getUser()
                ExploreEvent.UpdateToS -> updateToS()
                ExploreEvent.Error.ToSUpdate -> _exploreState.emit(ExploreState.Error.ToSUpdate)
                is ExploreEvent.Error.Generic -> _exploreState.emit(ExploreState.Error.Generic(event.stringId))
                is ExploreEvent.BlockContribution -> blockContribution(event.uuid)
                is ExploreEvent.BlockCulture -> blockCulture(event.uuid)
                is ExploreEvent.BlockElement -> blockElement(event.uuid)
                is ExploreEvent.FavouriteContribution -> favouriteContribution(event.uuid)
                is ExploreEvent.FavouriteCulture -> favouriteCulture(event.uuid)
                is ExploreEvent.FavouriteElement -> favouriteElement(event.uuid)
                is ExploreEvent.GoToContributionDetails ->
                    _exploreState.emit(
                        ExploreState.Navigate(event.contribution.id.toString(), true)
                    )
                is ExploreEvent.GoToElementDetails ->
                    _exploreState.emit(
                        ExploreState.Navigate(event.element.id.toString(), false)
                    )
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
            val response = elementsRepository.getElements(searchCriteriaState.toSearchCriteria())
            when (response) {
                is ApiResponse.Success -> {
                    val elements = response.data.map { element ->
                        val media = elementsRepository.getElementMedia(element.id)?.getDataOrNull()
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
            val response =
                elementsRepository.getContributions(searchCriteriaState.toSearchCriteria())
            when (response) {
                is ApiResponse.Success -> {
                    val contributions = response.data.map { contribution ->
                        val media =
                            elementsRepository.getContributionMedia(contribution.id)
                                ?.getDataOrNull()
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
            val response = elementsRepository.getCultures(searchCriteriaState.toSearchCriteria())
            when (response) {
                is ApiResponse.Success -> {
                    _exploreState.emit(ExploreState.Success.CulturesReceived(response.data))
                }
                else -> _exploreState.emit(ExploreState.Error.Generic(R.string.generic_sorry))
            }
        }
    }

    private fun blockElement(uuid: UUID?) {
        viewModelScope.launch {
            when (updateRepository.blockElement(uuid)) {
                is ApiResponse.Success, is ApiResponse.Success.Empty -> postEvent(ExploreEvent.GetElements)
                else -> postEvent(ExploreEvent.Error.Generic(R.string.generic_sorry))
            }
        }
    }

    private fun blockContribution(uuid: UUID?) {
        viewModelScope.launch {
            when (updateRepository.blockContribution(uuid)) {
                is ApiResponse.Success, is ApiResponse.Success.Empty -> postEvent(ExploreEvent.GetContributions)
                else -> postEvent(ExploreEvent.Error.Generic(R.string.generic_sorry))
            }
        }
    }

    private fun blockCulture(uuid: UUID?) {
        viewModelScope.launch {
            when (updateRepository.blockCulture(uuid)) {
                is ApiResponse.Success, is ApiResponse.Success.Empty -> postEvent(ExploreEvent.GetCultures)
                else -> postEvent(ExploreEvent.Error.Generic(R.string.generic_sorry))
            }
        }
    }

    private fun favouriteElement(uuid: UUID?) {
        viewModelScope.launch {
            when (updateRepository.favouriteElement(uuid)) {
                is ApiResponse.Success, is ApiResponse.Success.Empty -> postEvent(ExploreEvent.GetElements)
                else -> postEvent(ExploreEvent.Error.Generic(R.string.generic_sorry))
            }
        }
    }

    private fun favouriteContribution(uuid: UUID?) {
        viewModelScope.launch {
            when (updateRepository.favouriteContribution(uuid)) {
                is ApiResponse.Success, is ApiResponse.Success.Empty -> postEvent(ExploreEvent.GetContributions)
                else -> postEvent(ExploreEvent.Error.Generic(R.string.generic_sorry))
            }
        }
    }

    private fun favouriteCulture(uuid: UUID?) {
        viewModelScope.launch {
            when (updateRepository.favouriteCulture(uuid)) {
                is ApiResponse.Success, is ApiResponse.Success.Empty -> postEvent(ExploreEvent.GetCultures)
                else -> postEvent(ExploreEvent.Error.Generic(R.string.generic_sorry))
            }
        }
    }

    private val <T : Any> ApiResponse.Failure<T>.errorMessage
        get() = when (message) {
            "ToSUpdate", "PrivacyUpdate" -> R.string.tos_update
            else -> R.string.generic_sorry
        }
}