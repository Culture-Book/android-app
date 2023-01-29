package uk.co.culturebook.add_new.title_type.events

import uk.co.culturebook.data.models.cultural.ElementType

sealed interface TitleAndTypeEvent {
    object Idle : TitleAndTypeEvent
    data class Submit(val title: String, val type: ElementType) : TitleAndTypeEvent
}