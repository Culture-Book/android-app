package uk.co.culturebook.data.models.cultural

import kotlinx.serialization.Serializable
import uk.co.culturebook.data.serializers.UUIDSerializer
import java.util.UUID

@Serializable
data class SearchCriteria(
    val location: Location? = null,
    val searchString: String? = null,
    @Serializable(with = UUIDSerializer::class)
    val elementId: UUID? = null,
    @Serializable(with = UUIDSerializer::class)
    val contributionId: UUID? = null,
    val types: List<@Serializable ElementType> = emptyList(),
    val page: Int = 0,
    val radius: Double = 0.0
)