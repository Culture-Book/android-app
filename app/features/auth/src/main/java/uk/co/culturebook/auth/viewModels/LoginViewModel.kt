package uk.co.culturebook.auth.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.co.culturebook.auth.events.LoginEvent
import uk.co.culturebook.auth.states.LoginState
import uk.co.culturebook.data.logD
import uk.co.culturebook.data.models.authentication.User
import uk.co.culturebook.data.models.authentication.enums.RegistrationStatus
import uk.co.culturebook.data.remote.interfaces.ApiResponse
import uk.co.culturebook.data.repositories.authentication.UserRepository
import uk.co.culturebook.ui.R
import java.net.URI
import java.util.*

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    init {
        if (userRepository.isUserLoggedIn()) {
            viewModelScope.launch { _loginState.emit(LoginState.Success) }
        }
    }

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
            is ApiResponse.Success.Empty -> _loginState.emit(LoginState.Idle)
        }
    }

    private fun GoogleSignInAccount.toUser(): User? =
        email?.let { email ->
            id?.let { id ->
                User(
                    URI.create(photoUrl.toString()),
                    displayName,
                    UUID.nameUUIDFromBytes(id.toByteArray()).toString(),
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
            is ApiResponse.Success.Empty -> _loginState.emit(LoginState.Idle)
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