package uk.co.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import uk.co.culturebook.data.models.cultural.SearchType
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.largeRoundedShape
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.smallRoundedShape
import java.util.*

sealed interface BlockOptionsState {
    data class Hide(val id: UUID) : BlockOptionsState
    data class Report(val id: UUID) : BlockOptionsState
    data class Block(val id: UUID) : BlockOptionsState
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeFilters(
    modifier: Modifier = Modifier,
    type: SearchType,
    onTypeChanged: (SearchType) -> Unit,
    showFavourites: Boolean = false,
    onShowFavourites: ((Boolean) -> Unit)? = null
) {
    LazyRow(
        modifier = modifier
    ) {
        if (onShowFavourites != null) {
            item {
                FilterChip(
                    modifier = Modifier.padding(horizontal = mediumSize),
                    selected = showFavourites,
                    onClick = { onShowFavourites(!showFavourites) },
                    label = { Text(stringResource(R.string.favourites)) },
                    shape = smallRoundedShape
                )
            }
        }

        item {
            FilterChip(
                selected = type == SearchType.Element,
                onClick = { onTypeChanged(SearchType.Element) },
                label = { Text(stringResource(R.string.elements)) },
                shape = largeRoundedShape
            )
        }
        item {
            FilterChip(
                modifier = Modifier.padding(horizontal = mediumSize),
                selected = type == SearchType.Contribution,
                onClick = { onTypeChanged(SearchType.Contribution) },
                label = { Text(stringResource(R.string.contributions)) },
                shape = largeRoundedShape
            )
        }
        item {
            FilterChip(
                selected = type == SearchType.Culture,
                onClick = { onTypeChanged(SearchType.Culture) },
                label = { Text(stringResource(R.string.cultures)) },
                shape = largeRoundedShape
            )
        }
    }
}