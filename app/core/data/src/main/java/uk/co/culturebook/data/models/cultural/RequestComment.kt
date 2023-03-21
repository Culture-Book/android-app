package uk.co.culturebook.data.models.cultural

import kotlinx.serialization.Serializable
import uk.co.culturebook.data.serializers.UUIDSerializer
import java.util.*

@Serializable
data class RequestComment(
    @Serializable(with = UUIDSerializer::class)
    val elementId: UUID? = null,
    @Serializable(with = UUIDSerializer::class)
    val contributionId: UUID? = null,
    val comment: Comment
)
