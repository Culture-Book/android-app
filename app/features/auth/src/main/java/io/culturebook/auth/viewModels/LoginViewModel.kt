package io.culturebook.auth.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.culturebook.auth.events.LoginEvent
import io.culturebook.auth.states.LoginState
import io.culturebook.data.logD
import io.culturebook.data.models.authentication.User
import io.culturebook.data.models.authentication.enums.RegistrationStatus
import io.culturebook.data.remote.interfaces.ApiResponse
import io.culturebook.data.repositories.authentication.UserRepository
import io.culturebook.ui.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.URI

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
                is LoginEvent.Error -> _loginState.emit(LoginState.Error(loginEvent.messageId))
                is LoginEvent.GoogleLogin -> registerOrLogin(loginEvent.googleSignInAccount.toUser())
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

    private fun GoogleSignInAccount.toUser(): User? =
        email?.let { email ->
            id?.let { id ->
                User(
                    URI.create(photoUrl.toString()),
                    displayName,
                    id,
                    email,
                    registrationStatus = RegistrationStatus.Registered.ordinal
                )
            }
        }

    private suspend fun registerOrLogin(user: User?) {
        _loginState.emit(LoginState.Loading)
        when (val loginRes = userRepository.registerOrLogin(user)) {
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
    private val <T : Any> ApiResponse.Exception<T>.errorMessage: Int
        get() {
            throwable.message.logD().also { return R.string.generic_sorry }
        }
}