package uk.co.culturebook.data.models.authentication

import kotlinx.serialization.Serializable
import uk.co.culturebook.data_access.serializers.UUIDSerializer
import java.util.*

@Serializable
data class PasswordReset(
    @Serializable(with = UUIDSerializer::class)
    val passwordResetToken: UUID,
    val email: String,
    val password: String,
)