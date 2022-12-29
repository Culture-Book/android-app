package io.culturebook.auth.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.culturebook.auth.events.LoginEvent
import io.culturebook.auth.states.LoginState
import io.culturebook.data.logD
import io.culturebook.data.models.authentication.User
import io.culturebook.data.remote.interfaces.ApiResponse
import io.culturebook.data.repositories.authentication.UserRepository
import io.culturebook.ui.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    fun postEvent(loginEvent: LoginEvent) {
        viewModelScope.launch {
            when (loginEvent) {
                LoginEvent.Idle -> _loginState.emit(LoginState.Idle)
                is LoginEvent.Login ->
                    login(loginEvent.email, loginEvent.password)
            }
        }
    }

    private suspend fun login(email: String, password: String) {
        _loginState.emit(LoginState.Loading)
        when (val loginRes = userRepository.login(User(email = email, password = password))) {
            is ApiResponse.Success -> {
                userRepository.saveUserToken(loginRes.data)
                _loginState.emit(LoginState.Success)
            }
            is ApiResponse.Failure -> _loginState.emit(LoginState.Error(loginRes.errorMessage))
            is ApiResponse.Exception -> _loginState.emit(LoginState.Error(loginRes.errorMessage))
        }
    }

    private val <T : Any> ApiResponse.Failure<T>.errorMessage
        get() = when (message) {
            "DuplicateEmail" -> R.string.duplicate
            "InvalidEmail" -> R.string.invalid_email
            else -> R.string.generic_sorry
        }
    private val <T : Any> ApiResponse.Exception<T>.errorMessage
        get() = throwable.message.logD()?.also { R.string.generic_sorry } ?: R.string.generic_sorry
}