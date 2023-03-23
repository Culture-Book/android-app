package uk.co.culturebook.data.models.cultural

import kotlinx.serialization.Serializable
import uk.co.culturebook.data.serializers.UUIDSerializer
import java.util.*

@Serializable
data class BlockedCulture(
    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID,
    val name: String
)
