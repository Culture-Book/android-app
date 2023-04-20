package uk.co.culturebook.data.models.cultural

import kotlinx.serialization.Serializable
import uk.co.culturebook.data.serializers.UUIDSerializer
import java.util.UUID

@Serializable
data class BlockedElement(
    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID,
    val name: String = ""
)
