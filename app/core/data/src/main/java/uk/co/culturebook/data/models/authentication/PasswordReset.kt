package uk.co.culturebook.data.models.authentication

import kotlinx.serialization.Serializable
import uk.co.culturebook.data.serializers.UUIDSerializer
import java.util.UUID

@Serializable
data class PasswordReset(
    @Serializable(with = UUIDSerializer::class)
    val token: UUID,
    val userId: String,
    val password: String,
)