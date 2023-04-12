package uk.co.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import uk.co.culturebook.data.models.cultural.ElementType
import uk.co.culturebook.data.models.cultural.allElementTypes
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.*

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElementTypesComposable(
    modifier: Modifier = Modifier,
    types: List<ElementType> = allElementTypes,
    selectedType: ElementType? = null,
    selectedTypes: List<ElementType> = emptyList(),
    onTypeClicked: (ElementType) -> Unit = {}
) {
    val localConfig = LocalConfiguration.current

    if (localConfig.screenWidthDp >= MinScreenWidth) {
        LazyVerticalGrid(modifier = modifier, columns = GridCells.Fixed(2)) {
            items(types) {
                val isSelected = it == selectedType || selectedTypes.contains(it)
                FilterChip(
                    modifier = Modifier.padding(end = smallSize, bottom = smallSize),
                    selected = isSelected,
                    shape = largeRoundedShape,
                    onClick = { onTypeClicked(it) },
                    leadingIcon = {
                        if (isSelected) Icon(
                            modifier = Modifier.padding(smallSize),
                            painter = AppIcon.Tick.getPainter(),
                            contentDescription = "Tick"
                        ) else Icon(
                            modifier = Modifier.padding(smallSize),
                            painter = it.icon,
                            contentDescription = "Type icon"
                        )
                    },
                    label = {
                        Text(modifier = Modifier.padding(vertical = mediumSize), text = it.label)
                    }
                )
            }
        }
    } else {
        LazyColumn(modifier = modifier) {
            items(types) {
                val isSelected = it == selectedType || selectedTypes.contains(it)
                FilterChip(
                    modifier = Modifier
                        .padding(bottom = smallSize)
                        .fillMaxWidth(),
                    selected = isSelected,
                    shape = largeRoundedShape,
                    onClick = { onTypeClicked(it) },
                    leadingIcon = {
                        if (isSelected) Icon(
                            modifier = Modifier.padding(smallSize),
                            painter = AppIcon.Tick.getPainter(),
                            contentDescription = "Tick"
                        ) else Icon(
                            modifier = Modifier.padding(smallSize),
                            painter = it.icon,
                            contentDescription = "Type icon"
                        )
                    },
                    label = {
                        Text(modifier = Modifier.padding(vertical = mediumSize), text = it.label)
                    }
                )
            }
        }
    }
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElementTypesComposableHorizontal(
    modifier: Modifier = Modifier,
    types: List<ElementType> = allElementTypes,
    selectedType: ElementType? = null,
    selectedTypes: List<ElementType> = emptyList(),
    onTypeClicked: (ElementType) -> Unit = {}
) {
    LazyRow(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        items(types) {
            val isSelected = it == selectedType || selectedTypes.contains(it)
            FilterChip(
                modifier = Modifier.padding(end = smallSize, bottom = smallSize),
                selected = isSelected,
                shape = largeRoundedShape,
                onClick = { onTypeClicked(it) },
                leadingIcon = {
                    if (isSelected) Icon(
                        modifier = Modifier.padding(smallSize),
                        painter = AppIcon.Tick.getPainter(),
                        contentDescription = "Tick"
                    ) else Icon(
                        modifier = Modifier.padding(smallSize),
                        painter = it.icon,
                        contentDescription = "Type icon"
                    )
                },
                label = {
                    Text(modifier = Modifier.padding(vertical = mediumSize), text = it.label)
                }
            )
        }
    }
}

val ElementType.label
    @Composable get() = when (this) {
        ElementType.Food -> stringResource(R.string.food)
        ElementType.Music -> stringResource(R.string.music)
        ElementType.Story -> stringResource(R.string.story)
        ElementType.PoI -> stringResource(R.string.poi)
        ElementType.Event -> stringResource(R.string.event)
        else -> ""
    }

val ElementType.icon
    @Composable get() = when (this) {
        ElementType.Food -> AppIcon.Food.getPainter()
        ElementType.Music -> AppIcon.Music.getPainter()
        ElementType.Story -> AppIcon.Story.getPainter()
        ElementType.PoI -> AppIcon.PointOfInterest.getPainter()
        ElementType.Event -> AppIcon.Event.getPainter()
        else -> AppIcon.Vase.getPainter()
    }