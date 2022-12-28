package io.culturebook.data.models.authentication

import io.culturebook.data.models.authentication.enums.RegistrationStatus
import io.culturebook.data.models.authentication.enums.VerificationStatus
import io.culturebook.data.serializers.URISerializer
import kotlinx.serialization.Serializable
import java.net.URI

@Serializable
data class User(
    @Serializable(with = URISerializer::class)
    val profileUri: URI? = null,
    val displayName: String? = null,
    val password: String,
    val email: String,
    val verificationStatus: Int = VerificationStatus.NotVerified.ordinal,
    val registrationStatus: Int = RegistrationStatus.Pending.ordinal
)
