package uk.co.culturebook.data.models.cultural

import kotlinx.serialization.Serializable
import uk.co.culturebook.data_access.serializers.UUIDSerializer
import java.util.*

@Serializable
data class Contribution(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = null,
    @Serializable(with = UUIDSerializer::class)
    val elementId: UUID,
    val name: String,
    val type: ElementType,
    val location: Location,
    val information: String,
    val eventType: EventType? = null,
    val linkElements: List<@Serializable(with = UUIDSerializer::class) UUID> = emptyList(),
    val media: List<Media> = emptyList(),
    val favourite: Boolean = false
)