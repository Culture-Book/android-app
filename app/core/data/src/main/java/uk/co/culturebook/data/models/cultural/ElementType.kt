package uk.co.culturebook.data.models.cultural

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable
import uk.co.culturebook.data.serializers.LocalDateTimeSerializer
import java.time.LocalDateTime

@Serializable
enum class ElementType {
    Food,
    Music,
    Story,
    PoI,
    Event
}

val allElementTypes = listOf(
    ElementType.Food,
    ElementType.Music,
    ElementType.Story,
    ElementType.PoI,
    ElementType.Event
)

@Serializable
@Stable
data class EventType(
    @Serializable(with = LocalDateTimeSerializer::class)
    val startDateTime: LocalDateTime,
    val location: Location
)

val EventType.Companion.Empty get() = EventType(LocalDateTime.MIN, Location.Empty)
val EventType.isEmpty get() = location.isEmpty() && startDateTime == LocalDateTime.MIN

fun String.decodeElementType() = when {
    equals(ElementType.Food.name) -> ElementType.Food
    equals(ElementType.Music.name) -> ElementType.Music
    equals(ElementType.Story.name) -> ElementType.Story
    equals(ElementType.PoI.name) -> ElementType.PoI
    equals(ElementType.Event.name) -> ElementType.Event

    else -> throw IllegalArgumentException()
}


