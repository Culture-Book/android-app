package io.culturebook.auth.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.culturebook.auth.events.LoginEvent
import io.culturebook.auth.states.LoginState
import io.culturebook.data.models.authentication.User
import io.culturebook.data.remote.interfaces.ApiResponse
import io.culturebook.data.repositories.authentication.UserRepository
import io.culturebook.nav.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    fun postEvent(loginEvent: LoginEvent) {
        when (loginEvent) {
            LoginEvent.Idle -> {}
            is LoginEvent.Login -> viewModelScope.launch {
                login(loginEvent.email, loginEvent.password)
            }
            LoginEvent.Register -> viewModelScope.launch {
                _loginState.emit(LoginState.Redirection(Route.Registration.route))
            }
        }
    }

    private suspend fun login(email: String, password: String) {
        when (val loginRes = userRepository.login(User(email = email, password = password))) {
            is ApiResponse.Success -> {
                userRepository.saveUserToken(loginRes.data)
                _loginState.emit(LoginState.Redirection(Route.Nearby.route))
            }
            is ApiResponse.Failure -> _loginState.emit(LoginState.Error(loginRes.errorMessage))
            is ApiResponse.Exception -> _loginState.emit(LoginState.Error(loginRes.errorMessage))
        }
    }

    private val <T : Any> ApiResponse.Failure<T>.errorMessage get() = message ?: ""
    private val <T : Any> ApiResponse.Exception<T>.errorMessage get() = throwable.message ?: ""
}