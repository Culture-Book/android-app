package uk.co.culturebook.data.models.cultural

import kotlinx.serialization.Serializable
import uk.co.culturebook.data.serializers.UUIDSerializer
import java.util.*

@Serializable
data class Element(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = null,
    @Serializable(with = UUIDSerializer::class)
    val cultureId: UUID,
    val name: String,
    val type: ElementType,
    val location: Location,
    val information: String,
    val eventType: EventType? = null,
    val linkElements: List<@Serializable(with = UUIDSerializer::class) UUID> = emptyList(),
    val media: List<Media> = emptyList(),
    val comments: List<Comment> = emptyList(),
    val reactions: List<Reaction> = emptyList(),
    val favourite: Boolean = false,
    val isVerified: Boolean = false
)