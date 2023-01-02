package uk.co.culturebook.auth.events

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

sealed interface LoginEvent {
    data class Login(val email: String, val password: String) : LoginEvent
    data class GoogleLogin(val googleSignInAccount: GoogleSignInAccount) : LoginEvent
    data class Error(@StringRes val messageId: Int) : LoginEvent
    object Idle : LoginEvent
}

@Stable
class LoginHState {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
}