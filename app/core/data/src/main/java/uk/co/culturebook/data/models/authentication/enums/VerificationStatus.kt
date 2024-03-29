package uk.co.culturebook.data.models.authentication.enums

enum class VerificationStatus {
    NotVerified, Verified, Pending
}

fun Int.toVerificationStatus() = VerificationStatus.values()[this]