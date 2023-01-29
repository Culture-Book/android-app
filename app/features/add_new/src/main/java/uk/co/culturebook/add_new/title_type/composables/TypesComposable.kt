package uk.co.culturebook.add_new.title_type.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    onTypeClicked: (ElementType) -> Unit = {}
) {
    LazyVerticalGrid(modifier = modifier, columns = GridCells.Fixed(2)) {
        items(types) {
            val label = when (it) {
                ElementType.Food -> stringResource(R.string.food)
                ElementType.Music -> stringResource(R.string.music)
                ElementType.Story -> stringResource(R.string.story)
                ElementType.PoI -> stringResource(R.string.poi)
                ElementType.Event -> stringResource(R.string.event)
            }
            val icon = when (it) {
                ElementType.Food -> AppIcon.Food.getPainter()
                ElementType.Music -> AppIcon.Music.getPainter()
                ElementType.Story -> AppIcon.Story.getPainter()
                ElementType.PoI -> AppIcon.PointOfInterest.getPainter()
                ElementType.Event -> AppIcon.Event.getPainter()
            }
            val isSelected = it == selectedType
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
                        painter = icon,
                        contentDescription = "Type icon"
                    )
                },
                label = {
                    Text(modifier = Modifier.padding(vertical = mediumSize), text = label)
                }
            )
        }
    }
}