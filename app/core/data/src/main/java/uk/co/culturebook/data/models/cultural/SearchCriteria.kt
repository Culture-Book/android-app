package uk.co.culturebook.data.models.cultural

import kotlinx.serialization.Serializable
import uk.co.culturebook.data_access.serializers.UUIDSerializer
import java.util.*

@Serializable
data class SearchCriteria(
    val location: Location? = null,
    val searchString: String? = null,
    @Serializable(with = UUIDSerializer::class)
    val elementId: UUID? = null,
    @Serializable(with = UUIDSerializer::class)
    val contributionId: UUID? = null,
    val types: List<@Serializable ElementType> = emptyList(),
    val page: Int = 1,
    val radius: Double = 3.0,
)
