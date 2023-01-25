package uk.co.culturebook.data.models.cultural

import kotlinx.serialization.Serializable
import uk.co.culturebook.data_access.serializers.UUIDSerializer
import java.util.*

@Serializable
data class Culture(
    @Serializable(with = UUIDSerializer::class) val id: UUID? = null,
    val name: String,
    val location: Location
)

@Serializable
data class CultureResponse(val cultures: List<Culture>)