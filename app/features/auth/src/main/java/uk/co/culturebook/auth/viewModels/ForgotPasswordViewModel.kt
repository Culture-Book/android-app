package uk.co.culturebook.auth.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.co.culturebook.auth.states.ForgotState
import uk.co.culturebook.data.repositories.authentication.UserRepository
import java.util.*

class ForgotPasswordViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _forgotState = MutableStateFlow<ForgotState>(ForgotState.Idle)
    val forgotState = _forgotState.asStateFlow()

    fun requestPasswordReset(email: String) {
        viewModelScope.launch {
            userRepository.requestPasswordReset(email)
        }
    }

    fun passwordReset(userId: String, password: String, passwordResetToken: String) {
        viewModelScope.launch {
            userRepository.passwordReset(userId, password, UUID.fromString(passwordResetToken))
        }
    }
}