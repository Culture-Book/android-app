package uk.co.culturebook.data.models.cultural

import kotlinx.serialization.Serializable

@Serializable
data class BucketRequest(
    val id: String,
    val name: String,
    val public: Boolean = false
)
