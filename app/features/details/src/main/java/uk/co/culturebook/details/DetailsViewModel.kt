package uk.co.culturebook.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.co.culturebook.data.models.cultural.Comment
import uk.co.culturebook.data.models.cultural.SearchCriteriaState
import uk.co.culturebook.data.remote.interfaces.ApiResponse
import uk.co.culturebook.data.repositories.cultural.DetailsRepository
import uk.co.culturebook.data.repositories.cultural.ElementsRepository
import uk.co.culturebook.data.repositories.cultural.UpdateRepository
import uk.co.culturebook.ui.R
import java.util.UUID

// Todo break apart the show contributions logic.
class DetailsViewModel(
    private val elementsRepository: ElementsRepository,
    private val updateRepository: UpdateRepository,
    private val detailsRepository: DetailsRepository
) : ViewModel() {
    private val _detailStateFlow = MutableStateFlow<DetailState>(DetailState.Idle)
    val detailStateFlow = _detailStateFlow.asStateFlow()
    val searchCriteria = SearchCriteriaState()

    fun postEvent(event: DetailEvent, showLoading: Boolean = true) {
        viewModelScope.launch {
            if (showLoading) {
                _detailStateFlow.value = DetailState.Loading
            }
            when (event) {
                DetailEvent.Idle -> _detailStateFlow.emit(DetailState.Idle)
                is DetailEvent.BlockContribution -> blockContribution(event.uuid)
                is DetailEvent.BlockElement -> blockElement(event.uuid)
                is DetailEvent.FavouriteContribution -> favouriteContribution(event.contributionId)
                is DetailEvent.FavouriteElement -> favouriteElement(event.element.id)
                is DetailEvent.GetContribution -> getContribution(event.uuid)
                is DetailEvent.GetElement -> getElement(event.uuid)
                is DetailEvent.AddElementComment -> addElementComment(event.parentId, event.comment)
                is DetailEvent.ToggleElementReaction -> toggleElementReaction(
                    event.parentId,
                    event.reaction
                )

                is DetailEvent.BlockElementComment -> blockElementComment(
                    event.elementId,
                    Comment(event.id)
                )

                is DetailEvent.GetContributions -> getContributions(event.uuid)
                is DetailEvent.DeleteElementComment -> deleteElementComment(
                    event.elementId,
                    Comment(event.id)
                )

                is DetailEvent.AddContributionComment -> addContributionComment(
                    event.parentId,
                    event.comment
                )

                is DetailEvent.BlockContributionComment -> blockContributionComment(
                    event.contributionId,
                    Comment(event.id)
                )

                is DetailEvent.DeleteContributionComment -> deleteContributionComment(
                    event.contributionId,
                    Comment(event.id)
                )

                is DetailEvent.ToggleContributionReaction -> toggleContributionReaction(
                    event.contributionId,
                    event.reaction
                )

                is DetailEvent.GetContributionComments -> getContributionComments(event.contributionId)
                is DetailEvent.GetElementComments -> getElementComments(event.elementId)
                is DetailEvent.FavouriteFromShowContributions -> favouriteContribution(
                    event.contributionId,
                    event.elementId
                )

                is DetailEvent.BlockFromShowContributions -> blockContributionFromShowContributions(
                    event.contributionId,
                    event.elementId
                )
            }
        }
    }

    private suspend fun getElement(uuid: UUID?) {
        when (val response = elementsRepository.getElement(uuid)) {
            is ApiResponse.Success -> _detailStateFlow.emit(
                DetailState.ElementReceived(response.data)
            )

            else -> _detailStateFlow.emit(DetailState.Error(R.string.generic_sorry))
        }
    }

    private suspend fun getContribution(uuid: UUID?) {
        when (val response = elementsRepository.getContribution(uuid)) {
            is ApiResponse.Success -> _detailStateFlow.emit(
                DetailState.ContributionReceived(response.data)
            )

            else -> _detailStateFlow.emit(DetailState.Error(R.string.generic_sorry))
        }
    }

    private suspend fun blockElement(uuid: UUID?) {
        when (updateRepository.blockElement(uuid)) {
            is ApiResponse.Success, is ApiResponse.Success.Empty ->
                _detailStateFlow.emit(DetailState.Blocked)

            else -> _detailStateFlow.emit(DetailState.Error(R.string.generic_sorry))
        }
    }

    private suspend fun blockContribution(uuid: UUID?) {
        when (updateRepository.blockContribution(uuid)) {
            is ApiResponse.Success, is ApiResponse.Success.Empty ->
                _detailStateFlow.emit(DetailState.Blocked)

            else -> _detailStateFlow.emit(DetailState.Error(R.string.generic_sorry))
        }
    }

    private suspend fun blockContributionFromShowContributions(uuid: UUID?, elementId: UUID) {
        when (updateRepository.blockContribution(uuid)) {
            is ApiResponse.Success, is ApiResponse.Success.Empty ->
                postEvent(DetailEvent.GetContributions(elementId))

            else -> _detailStateFlow.emit(DetailState.Error(R.string.generic_sorry))
        }
    }

    private suspend fun favouriteElement(uuid: UUID?) {
        when (updateRepository.favouriteElement(uuid)) {
            is ApiResponse.Success, is ApiResponse.Success.Empty ->
                postEvent(DetailEvent.GetElement(uuid))

            else -> _detailStateFlow.emit(DetailState.Error(R.string.generic_sorry))
        }
    }

    private suspend fun favouriteContribution(uuid: UUID?) {
        when (updateRepository.favouriteContribution(uuid)) {
            is ApiResponse.Success, is ApiResponse.Success.Empty ->
                postEvent(DetailEvent.GetContribution(uuid))

            else -> _detailStateFlow.emit(DetailState.Error(R.string.generic_sorry))
        }
    }

    private suspend fun favouriteContribution(uuid: UUID?, elementId: UUID) {
        when (updateRepository.favouriteContribution(uuid)) {
            is ApiResponse.Success, is ApiResponse.Success.Empty ->
                postEvent(DetailEvent.GetContributions(elementId))

            else -> _detailStateFlow.emit(DetailState.Error(R.string.generic_sorry))
        }
    }

    private suspend fun addElementComment(uuid: UUID?, comment: String) {
        when (detailsRepository.addElementComment(uuid!!, comment)) {
            is ApiResponse.Success, is ApiResponse.Success.Empty ->
                postEvent(DetailEvent.GetElementComments(uuid), false)

            else -> _detailStateFlow.emit(DetailState.Error(R.string.generic_sorry))
        }
    }

    private suspend fun addContributionComment(uuid: UUID?, comment: String) {
        when (detailsRepository.addContributionComment(uuid!!, comment)) {
            is ApiResponse.Success, is ApiResponse.Success.Empty ->
                postEvent(DetailEvent.GetContributionComments(uuid), false)

            else -> _detailStateFlow.emit(DetailState.Error(R.string.generic_sorry))
        }
    }

    private suspend fun blockElementComment(uuid: UUID?, comment: Comment) {
        when (detailsRepository.blockElementComment(uuid!!, comment.id)) {
            is ApiResponse.Success, is ApiResponse.Success.Empty ->
                postEvent(DetailEvent.GetElementComments(uuid), false)

            else -> _detailStateFlow.emit(DetailState.Error(R.string.generic_sorry))
        }
    }

    private suspend fun blockContributionComment(uuid: UUID?, comment: Comment) {
        when (detailsRepository.blockContributionComment(uuid!!, comment.id)) {
            is ApiResponse.Success, is ApiResponse.Success.Empty ->
                postEvent(DetailEvent.GetContributionComments(uuid), false)

            else -> _detailStateFlow.emit(DetailState.Error(R.string.generic_sorry))
        }
    }

    private suspend fun deleteElementComment(uuid: UUID?, comment: Comment) {
        when (detailsRepository.deleteElementComment(comment.id)) {
            is ApiResponse.Success, is ApiResponse.Success.Empty ->
                postEvent(DetailEvent.GetElementComments(uuid!!), false)

            else -> _detailStateFlow.emit(DetailState.Error(R.string.generic_sorry))
        }
    }

    private suspend fun deleteContributionComment(uuid: UUID?, comment: Comment) {
        when (detailsRepository.deleteContributionComment(comment.id)) {
            is ApiResponse.Success, is ApiResponse.Success.Empty ->
                postEvent(DetailEvent.GetContributionComments(uuid!!), false)

            else -> _detailStateFlow.emit(DetailState.Error(R.string.generic_sorry))
        }
    }

    private suspend fun toggleElementReaction(uuid: UUID?, reaction: String) {
        when (detailsRepository.toggleElementReaction(uuid!!, reaction)) {
            is ApiResponse.Success, is ApiResponse.Success.Empty ->
                postEvent(DetailEvent.GetElement(uuid))

            else -> _detailStateFlow.emit(DetailState.Error(R.string.generic_sorry))
        }
    }

    private suspend fun toggleContributionReaction(uuid: UUID?, reaction: String) {
        when (detailsRepository.toggleContributionReaction(uuid!!, reaction)) {
            is ApiResponse.Success, is ApiResponse.Success.Empty ->
                postEvent(DetailEvent.GetContribution(uuid))

            else -> _detailStateFlow.emit(DetailState.Error(R.string.generic_sorry))
        }
    }

    private suspend fun getElementComments(uuid: UUID?) {
        when (val response = detailsRepository.getElementComments(uuid!!)) {
            is ApiResponse.Success -> _detailStateFlow.emit(
                DetailState.ElementCommentsReceived(response.data)
            )

            else -> _detailStateFlow.emit(DetailState.Error(R.string.generic_sorry))
        }
    }

    private suspend fun getContributionComments(uuid: UUID?) {
        when (val response = detailsRepository.getContributionComments(uuid!!)) {
            is ApiResponse.Success -> _detailStateFlow.emit(
                DetailState.ContributionCommentsReceived(response.data)
            )

            else -> _detailStateFlow.emit(DetailState.Error(R.string.generic_sorry))
        }
    }

    private suspend fun getContributions(uuid: UUID?) {
        searchCriteria.elementId = uuid
        when (val response =
            elementsRepository.getContributions(searchCriteria.toSearchCriteria())) {
            is ApiResponse.Success -> _detailStateFlow.emit(
                DetailState.ContributionsReceived(response.data)
            )

            else -> _detailStateFlow.emit(DetailState.Error(R.string.generic_sorry))
        }
    }
}