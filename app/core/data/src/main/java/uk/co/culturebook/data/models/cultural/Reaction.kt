package uk.co.culturebook.data.models.cultural

import kotlinx.serialization.Serializable

@Serializable
data class Reaction(
    val reaction: String,
    val isMine: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Reaction

        if (reaction != other.reaction) return false

        return true
    }

    override fun hashCode(): Int {
        return reaction.hashCode()
    }
}