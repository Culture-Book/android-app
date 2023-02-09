package uk.co.culturebook.add_new.title_type.states

import androidx.annotation.StringRes
import uk.co.culturebook.data.models.cultural.Element
import uk.co.culturebook.data.models.cultural.ElementType
import uk.co.culturebook.ui.R

sealed interface TitleAndTypeState {
    object Idle : TitleAndTypeState
    object Loading : TitleAndTypeState
    data class Success(val title: String, val elementType: ElementType) : TitleAndTypeState
    open class Error(@StringRes val stringId: Int) : TitleAndTypeState {
        data class Duplicate(val element: Element) : Error(R.string.duplicate_element_title)
    }
}