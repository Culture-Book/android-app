package uk.co.culturebook.add_new.title_type.states

import androidx.annotation.StringRes
import uk.co.culturebook.data.models.cultural.Element
import uk.co.culturebook.data.models.cultural.ElementType

sealed interface TitleAndTypeState {
    object Idle : TitleAndTypeState
    object Loading : TitleAndTypeState
    data class Success(val title: String, val elementType: ElementType) : TitleAndTypeState
    data class Duplicate(val element: Element) : TitleAndTypeState
    data class Error(@StringRes val stringId: Int) : TitleAndTypeState
}