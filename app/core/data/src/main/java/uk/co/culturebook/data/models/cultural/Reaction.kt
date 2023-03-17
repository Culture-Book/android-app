package uk.co.culturebook.data.models.cultural

import kotlinx.serialization.Serializable

@Serializable
data class Reaction(
    val reaction: String,
    val isMine: Boolean = false
)