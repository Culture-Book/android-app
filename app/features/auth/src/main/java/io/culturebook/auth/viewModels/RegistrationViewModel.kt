package io.culturebook.auth.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.culturebook.auth.events.RegistrationEvent
import io.culturebook.auth.states.RegistrationState
import io.culturebook.data.logD
import io.culturebook.data.models.authentication.User
import io.culturebook.data.remote.interfaces.ApiResponse
import io.culturebook.data.repositories.authentication.UserRepository
import io.culturebook.ui.R
import io.culturebook.ui.theme.molecules.isNotValidEmail
import io.culturebook.ui.theme.molecules.isNotValidPassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState = _registrationState.asStateFlow()

    fun postEvent(event: RegistrationEvent) {
        viewModelScope.launch {
            when (event) {
                is RegistrationEvent.Idle -> _registrationState.emit(RegistrationState.Idle)
                is RegistrationEvent.Register -> if (validateEvent(event)) register(event)
            }
        }
    }

    private suspend fun register(event: RegistrationEvent.Register) {
        _registrationState.emit(RegistrationState.Loading)
        val user =
            User(email = event.email, password = event.password, displayName = event.displayName)
        when (val regRes = userRepository.register(user)) {
            is ApiResponse.Success -> {
                userRepository.saveUserToken(regRes.data)
                _registrationState.emit(RegistrationState.Success)
            }
            is ApiResponse.Failure -> _registrationState.emit(RegistrationState.Error(regRes.errorMessage))
            is ApiResponse.Exception -> _registrationState.emit(RegistrationState.Error(regRes.errorMessage))
        }
    }

    private fun validateEvent(event: RegistrationEvent.Register): Boolean {
        return if (event.email.isNotValidEmail()) {
            viewModelScope.launch { _registrationState.emit(RegistrationState.Error(R.string.email_invalid)) }
            false
        } else if (event.password.isNotValidPassword()) {
            viewModelScope.launch { _registrationState.emit(RegistrationState.Error(R.string.password_invalid)) }
            false
        } else if (!event.tosAccepted && !event.privacyAccepted) {
            viewModelScope.launch { _registrationState.emit(RegistrationState.Error(R.string.accept_tos)) }
            false
        } else {
            true
        }
    }


    private val <T : Any> ApiResponse.Failure<T>.errorMessage
        get() = when (message) {
            "DuplicateEmail" -> R.string.duplicate
            "InvalidEmail" -> R.string.invalid_email
            "InvalidPassword" -> R.string.invalid_password
            else -> R.string.generic_sorry
        }
    private val <T : Any> ApiResponse.Exception<T>.errorMessage: Int
        get() {
            throwable.message.logD().also { return R.string.generic_sorry }
        }
}