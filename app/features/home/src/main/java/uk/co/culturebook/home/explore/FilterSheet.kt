package uk.co.culturebook.home.explore

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import uk.co.common.ElementTypesComposable
import uk.co.culturebook.data.models.cultural.ElementType
import uk.co.culturebook.data.models.cultural.SearchCriteriaState
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.molecules.ModalBottomSheet
import uk.co.culturebook.ui.theme.molecules.TitleAndSubtitle

@Composable
fun FilterSheet(searchCriteriaState: SearchCriteriaState, onDismiss: () -> Unit, onConfirm: (SearchCriteriaState) -> Unit) {
    val localState = remember { searchCriteriaState.copy() }

    fun toggleSelection(elementType: ElementType) {
        if (localState.types.contains(elementType)) {
            localState.types -= elementType
        } else {
            localState.types += elementType
        }
    }

    ModalBottomSheet(onDismiss = onDismiss, onConfirm = { onConfirm(localState) }) {
        TitleAndSubtitle(
            modifier = Modifier.padding(bottom = mediumSize),
            title = stringResource(R.string.type_filter)
        )
        ElementTypesComposable(
            selectedTypes = localState.types,
            onTypeClicked = { toggleSelection(it) }
        )
    }
}