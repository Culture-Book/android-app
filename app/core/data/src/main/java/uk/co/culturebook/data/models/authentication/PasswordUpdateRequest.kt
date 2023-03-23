package uk.co.culturebook.data.models.authentication

@kotlinx.serialization.Serializable
data class PasswordUpdateRequest(
    val oldPassword: String,
    val newPassword: String,
)
