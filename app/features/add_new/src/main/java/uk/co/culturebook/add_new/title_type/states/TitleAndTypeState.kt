package uk.co.culturebook.add_new.title_type.states

import androidx.annotation.StringRes
import uk.co.culturebook.add_new.data.TypeData
import uk.co.culturebook.data.models.cultural.Contribution
import uk.co.culturebook.data.models.cultural.Element
import uk.co.culturebook.ui.R

sealed interface TitleAndTypeState {
    object Idle : TitleAndTypeState
    object Loading : TitleAndTypeState
    data class Success(val typeData: TypeData) : TitleAndTypeState

    open class Error(@StringRes val stringId: Int) : TitleAndTypeState {
        data class Duplicate(val element: Element) : Error(R.string.duplicate_element_title)
        data class DuplicateContribution(val contribution: Contribution) :
            Error(R.string.duplicate_contribution_title)
    }
}