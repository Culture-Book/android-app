package uk.co.culturebook.data.models.authentication

@kotlinx.serialization.Serializable
data class VerificationStatusRequest(
    val reason: String,
)
