package uk.co.culturebook.data.models.authentication

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(val jwt: String, val refreshJwt: String)
