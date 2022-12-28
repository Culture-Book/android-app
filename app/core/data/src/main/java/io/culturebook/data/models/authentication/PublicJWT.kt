package io.culturebook.data.models.authentication
import kotlinx.serialization.Serializable

@Serializable
data class PublicJWT(val jwt: String)
