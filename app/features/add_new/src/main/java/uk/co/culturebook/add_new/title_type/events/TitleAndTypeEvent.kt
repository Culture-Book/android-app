package uk.co.culturebook.add_new.title_type.events

import uk.co.culturebook.add_new.data.TypeData

sealed interface TitleAndTypeEvent {
    object Idle : TitleAndTypeEvent
    data class Submit(val typeData: TypeData) : TitleAndTypeEvent
    data class SubmitContribution(val typeData: TypeData) : TitleAndTypeEvent
}