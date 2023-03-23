package uk.co.culturebook.account.profile

import android.net.Uri
import uk.co.culturebook.data.models.authentication.User
import uk.co.culturebook.data.models.cultural.MediaFile

sealed interface ProfileEvent {
    object Idle : ProfileEvent
    object RemoveProfilePicture : ProfileEvent
    data class AddProfilePicture(val profilePicture: MediaFile) : ProfileEvent
    data class UpdateUser(val user: User) : ProfileEvent
    data class RequestVerificationStatus(val reason: String) : ProfileEvent
    data class UpdatePassword(val oldPassword: String, val newPassword: String) : ProfileEvent
}