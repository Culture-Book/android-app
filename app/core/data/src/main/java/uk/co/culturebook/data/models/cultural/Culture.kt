package uk.co.culturebook.data.models.cultural

import kotlinx.serialization.Serializable
import uk.co.culturebook.data.serializers.UUIDSerializer
import java.util.UUID

@Serializable
data class Culture(
    @Serializable(with = UUIDSerializer::class) val id: UUID? = null,
    val name: String,
    val location: Location,
    val favourite: Boolean = false,
    val isVerified: Boolean = false,
)

val Culture.Companion.Empty
    get() = Culture(
        id = null,
        name = "",
        location = Location.Empty
    )

@Serializable
data class CultureResponse(val cultures: List<Culture>)