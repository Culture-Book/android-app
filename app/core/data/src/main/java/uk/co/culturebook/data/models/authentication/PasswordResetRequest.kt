package uk.co.culturebook.data.models.authentication

import kotlinx.serialization.Serializable

@Serializable
data class PasswordResetRequest(val email: String)