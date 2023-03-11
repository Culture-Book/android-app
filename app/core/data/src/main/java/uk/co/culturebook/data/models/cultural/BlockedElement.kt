package uk.co.culturebook.data.models.cultural

import kotlinx.serialization.Serializable
import uk.co.culturebook.data_access.serializers.UUIDSerializer
import java.util.*

@Serializable
data class BlockedElement(
    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID
)