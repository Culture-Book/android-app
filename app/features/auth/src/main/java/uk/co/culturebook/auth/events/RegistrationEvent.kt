package uk.co.culturebook.auth.events

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed interface RegistrationEvent {
    data class Register(
        val email: String,
        val password: String,
        val displayName: String,
        val tosAccepted: Boolean,
        val privacyAccepted: Boolean
    ) : RegistrationEvent

    object Idle : RegistrationEvent
}

fun RegisterState.toEvent() =
    RegistrationEvent.Register(email, password, displayName, tosChecked, privacyChecked)

@Stable
class RegisterState {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var displayName by mutableStateOf("")
    var tosChecked by mutableStateOf(false)
    var privacyChecked by mutableStateOf(false)
}

