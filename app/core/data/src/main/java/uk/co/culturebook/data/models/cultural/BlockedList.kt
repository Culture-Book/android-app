package uk.co.culturebook.data.models.cultural

import kotlinx.serialization.Serializable

@Serializable
data class BlockedList(
    val blockedElement: List<BlockedElement> = emptyList(),
    val blockedContribution: List<BlockedContribution> = emptyList(),
    val blockedCulture: List<BlockedCulture> = emptyList()
)
