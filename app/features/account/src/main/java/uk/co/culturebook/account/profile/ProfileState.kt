package uk.co.culturebook.account.profile

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import uk.co.culturebook.data.models.authentication.User
import uk.co.culturebook.data.models.authentication.enums.VerificationStatus
import uk.co.culturebook.data.models.cultural.MediaFile
import uk.co.culturebook.ui.R
import java.net.URI

sealed interface ProfileState {
    object Idle : ProfileState
    object Loading : ProfileState
    object ProfilePictureRemoved : ProfileState
    object PasswordUpdated : ProfileState
    data class VerificationRequested(val user: User) : ProfileState
    data class UserUpdated(val user: User) : ProfileState
    data class ProfilePictureAdded(val profilePicture: MediaFile) : ProfileState
    data class Error(@StringRes val messageId: Int = R.string.generic_sorry) : ProfileState
}

@Stable
class ProfileUIState {
    var displayName by mutableStateOf("")
    var email by mutableStateOf("")
    var verificationStatus by mutableStateOf(VerificationStatus.NotVerified)
    var profilePicture by mutableStateOf<Uri?>(null)

    fun toUser() = User(
        displayName = displayName,
        email = email,
        password = "",
        verificationStatus = verificationStatus.ordinal,
        profileUri = URI.create(profilePicture.toString())
    )
}