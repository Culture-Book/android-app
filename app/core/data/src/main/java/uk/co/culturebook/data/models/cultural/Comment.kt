package uk.co.culturebook.data.models.cultural

import kotlinx.serialization.Serializable
import uk.co.culturebook.data.serializers.UUIDSerializer
import java.util.UUID

@Serializable
data class Comment(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
    val comment: String = "",
    val isMine: Boolean = false
)
