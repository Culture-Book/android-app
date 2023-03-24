package uk.co.culturebook.account.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.co.culturebook.data.models.authentication.User
import uk.co.culturebook.data.models.authentication.enums.toVerificationStatus
import uk.co.culturebook.data.models.cultural.MediaFile
import uk.co.culturebook.data.remote.interfaces.ApiResponse
import uk.co.culturebook.data.remote.interfaces.getDataOrNull
import uk.co.culturebook.data.repositories.authentication.UserRepository
import uk.co.culturebook.data.utils.toUri

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _state = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val state = _state.asStateFlow()
    val uiState = ProfileUIState().apply {
        viewModelScope.launch {
            val user = userRepository.getUser().getDataOrNull() ?: return@launch
            displayName = user.displayName ?: ""
            email = user.email
            verificationStatus = user.verificationStatus.toVerificationStatus()
            profilePicture = user.profileUri?.toUri()
        }
    }

    fun postEvent(event: ProfileEvent) {
        updateState(ProfileState.Loading)
        viewModelScope.launch {
            when (event) {
                ProfileEvent.RemoveProfilePicture -> removeProfilePicture()
                ProfileEvent.Idle -> updateState(ProfileState.Idle)
                is ProfileEvent.AddProfilePicture -> addProfilePicture(event.profilePicture)
                is ProfileEvent.UpdateUser -> updateUser(event.user)
                is ProfileEvent.RequestVerificationStatus -> requestVerificationStatus(event.reason)
                is ProfileEvent.UpdatePassword -> updatePassword(
                    event.oldPassword,
                    event.newPassword
                )
            }
        }
    }

    private suspend fun addProfilePicture(profilePicture: MediaFile) {
        when (userRepository.updateProfilePicture(profilePicture)) {
            is ApiResponse.Success.Empty,
            is ApiResponse.Success -> updateState(ProfileState.ProfilePictureAdded(profilePicture))
            is ApiResponse.Exception -> updateState(ProfileState.Error())
            is ApiResponse.Failure -> updateState(ProfileState.Error())
        }
    }

    private suspend fun removeProfilePicture() {
        when (userRepository.removeProfilePicture()) {
            is ApiResponse.Success.Empty,
            is ApiResponse.Success -> updateState(ProfileState.ProfilePictureRemoved).also {
                uiState.profilePicture = null
            }
            is ApiResponse.Exception -> updateState(ProfileState.Error())
            is ApiResponse.Failure -> updateState(ProfileState.Error())
        }
    }

    private suspend fun updateUser(user: User) {
        when (val response = userRepository.updateUser(user)) {
            is ApiResponse.Success -> updateState(ProfileState.UserUpdated(response.data))
            is ApiResponse.Success.Empty,
            is ApiResponse.Exception -> updateState(ProfileState.Error())
            is ApiResponse.Failure -> updateState(ProfileState.Error())
        }
    }

    private suspend fun requestVerificationStatus(reason: String) {
        when (val response = userRepository.requestVerificationStatus(reason)) {
            is ApiResponse.Success -> updateState(ProfileState.VerificationRequested(response.data))
            is ApiResponse.Success.Empty,
            is ApiResponse.Exception -> updateState(ProfileState.Error())
            is ApiResponse.Failure -> updateState(ProfileState.Error())
        }
    }

    private suspend fun updatePassword(oldPassword: String, newPassword: String) {
        when (userRepository.updatePassword(oldPassword, newPassword)) {
            is ApiResponse.Success.Empty,
            is ApiResponse.Success -> updateState(ProfileState.PasswordUpdated)
            is ApiResponse.Exception -> updateState(ProfileState.Error())
            is ApiResponse.Failure -> updateState(ProfileState.Error())
        }
    }

    private fun updateState(state: ProfileState) {
        _state.value = state
    }
}