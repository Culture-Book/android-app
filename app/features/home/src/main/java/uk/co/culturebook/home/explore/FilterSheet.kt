package uk.co.culturebook.home.explore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uk.co.common.ElementTypesComposable
import uk.co.culturebook.data.models.cultural.ElementType
import uk.co.culturebook.data.models.cultural.SearchCriteriaState
import uk.co.culturebook.data.models.cultural.SearchType
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.*
import uk.co.culturebook.ui.theme.molecules.ModalBottomSheet
import uk.co.culturebook.ui.theme.molecules.TitleAndSubtitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSheet(
    searchCriteriaState: SearchCriteriaState,
    onDismiss: () -> Unit,
    onConfirm: (SearchCriteriaState) -> Unit
) {
    val localConfig = LocalConfiguration.current
    val localState by remember { derivedStateOf { searchCriteriaState.copy() } }

    ModalBottomSheet(
        onDismiss = onDismiss,
        onConfirm = { onConfirm(localState) }) {

        @Composable
        fun list() {
            Column {
                FilterChip(
                    modifier = Modifier
                        .padding(bottom = smallSize)
                        .fillMaxWidth(),
                    shape = largeRoundedShape,
                    selected = localState.searchType == SearchType.Element,
                    onClick = { localState.searchType = SearchType.Element },
                    label = {
                        Text(
                            modifier = Modifier.padding(vertical = mediumSize),
                            text = stringResource(R.string.elements)
                        )
                    },
                    leadingIcon = {
                        if (localState.searchType == SearchType.Element) Icon(
                            modifier = Modifier.padding(smallSize),
                            painter = AppIcon.Tick.getPainter(),
                            contentDescription = "Tick"
                        ) else Icon(
                            modifier = Modifier.padding(smallSize),
                            painter = AppIcon.Castle.getPainter(),
                            contentDescription = "Castle icon"
                        )
                    },
                )
                FilterChip(
                    modifier = Modifier
                        .padding(bottom = smallSize)
                        .fillMaxWidth(),
                    shape = largeRoundedShape,
                    selected = localState.searchType == SearchType.Contribution,
                    onClick = { localState.searchType = SearchType.Contribution },
                    label = {
                        Text(
                            modifier = Modifier.padding(vertical = mediumSize),
                            text = stringResource(R.string.contributions)
                        )
                    },
                    leadingIcon = {
                        if (localState.searchType == SearchType.Contribution) Icon(
                            modifier = Modifier.padding(smallSize),
                            painter = AppIcon.Tick.getPainter(),
                            contentDescription = "Tick"
                        ) else Icon(
                            modifier = Modifier.padding(smallSize),
                            painter = AppIcon.Castle.getPainter(),
                            contentDescription = "Castle icon"
                        )
                    },
                )
                FilterChip(
                    modifier = Modifier
                        .padding(bottom = smallSize)
                        .fillMaxWidth(),
                    shape = largeRoundedShape,
                    selected = localState.searchType == SearchType.Culture,
                    onClick = { localState.searchType = SearchType.Culture },
                    label = {
                        Text(
                            modifier = Modifier.padding(vertical = mediumSize),
                            text = stringResource(R.string.cultures)
                        )
                    },
                    leadingIcon = {
                        if (localState.searchType == SearchType.Culture) Icon(
                            modifier = Modifier.padding(smallSize),
                            painter = AppIcon.Tick.getPainter(),
                            contentDescription = "Tick"
                        ) else Icon(
                            modifier = Modifier.padding(smallSize),
                            painter = AppIcon.Castle.getPainter(),
                            contentDescription = "Castle icon"
                        )
                    },
                )
            }
        }

        @Composable
        fun grid() {
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                item {
                    FilterChip(
                        modifier = Modifier.padding(end = smallSize, bottom = smallSize),
                        shape = largeRoundedShape,
                        selected = localState.searchType == SearchType.Element,
                        onClick = { localState.searchType = SearchType.Element },
                        label = {
                            Text(
                                modifier = Modifier.padding(vertical = mediumSize),
                                text = stringResource(R.string.elements)
                            )
                        },
                        leadingIcon = {
                            if (localState.searchType == SearchType.Element) Icon(
                                modifier = Modifier.padding(smallSize),
                                painter = AppIcon.Tick.getPainter(),
                                contentDescription = "Tick"
                            ) else Icon(
                                modifier = Modifier.padding(smallSize),
                                painter = AppIcon.Castle.getPainter(),
                                contentDescription = "Castle icon"
                            )
                        },
                    )
                }
                item {
                    FilterChip(
                        modifier = Modifier.padding(end = smallSize, bottom = smallSize),
                        shape = largeRoundedShape,
                        selected = localState.searchType == SearchType.Contribution,
                        onClick = { localState.searchType = SearchType.Contribution },
                        label = {
                            Text(
                                modifier = Modifier.padding(vertical = mediumSize),
                                text = stringResource(R.string.contributions)
                            )
                        },
                        leadingIcon = {
                            if (localState.searchType == SearchType.Contribution) Icon(
                                modifier = Modifier.padding(smallSize),
                                painter = AppIcon.Tick.getPainter(),
                                contentDescription = "Tick"
                            ) else Icon(
                                modifier = Modifier.padding(smallSize),
                                painter = AppIcon.Castle.getPainter(),
                                contentDescription = "Castle icon"
                            )
                        },
                    )
                }
                item {
                    FilterChip(
                        modifier = Modifier.padding(end = smallSize, bottom = smallSize),
                        shape = largeRoundedShape,
                        selected = localState.searchType == SearchType.Culture,
                        onClick = { localState.searchType = SearchType.Culture },
                        label = {
                            Text(
                                modifier = Modifier.padding(vertical = mediumSize),
                                text = stringResource(R.string.cultures)
                            )
                        },
                        leadingIcon = {
                            if (localState.searchType == SearchType.Culture) Icon(
                                modifier = Modifier.padding(smallSize),
                                painter = AppIcon.Tick.getPainter(),
                                contentDescription = "Tick"
                            ) else Icon(
                                modifier = Modifier.padding(smallSize),
                                painter = AppIcon.Castle.getPainter(),
                                contentDescription = "Castle icon"
                            )
                        },
                    )
                }
            }
        }


        Column(
            modifier = Modifier.heightIn(
                min = localConfig.screenHeightDp.dp / 3,
                max = localConfig.screenHeightDp.dp * 10
            )
        ) {

            TitleAndSubtitle(
                modifier = Modifier.padding(bottom = mediumSize),
                title = stringResource(R.string.type_filter)
            )

            ElementTypesComposable(
                selectedTypes = localState.types,
                onTypeClicked = { localState.toggleTypesSelection(it) }
            )

            TitleAndSubtitle(
                modifier = Modifier.padding(bottom = mediumSize),
                title = stringResource(R.string.type_filter)
            )

            if (localConfig.screenWidthDp < MinScreenWidth) {
                list()
            } else {
                grid()
            }
        }
    }
}