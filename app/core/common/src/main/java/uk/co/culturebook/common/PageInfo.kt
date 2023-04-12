package uk.co.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import uk.co.culturebook.ui.theme.AppIcon

@Composable
fun PageInformation(
    modifier: Modifier,
    items: List<*>,
    currentPage: Int = 1,
    onNextPage: (Int) -> Unit,
    onPreviousPage: (Int) -> Unit,
    minPage: Int = 1,
) {
    val maxPage = if (items.size < 3) currentPage else currentPage + 1

    if (minPage == maxPage) return

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (minPage < currentPage) {
            FilledTonalIconButton(
                onClick = {
                    val prevPage = (currentPage - 1).coerceAtLeast(minPage)
                    onPreviousPage(prevPage)
                }) {
                Icon(
                    painter = AppIcon.ChevronLeft.getPainter(), contentDescription = "previous page"
                )
            }
        }

        if (maxPage > currentPage) {
            FilledTonalIconButton(onClick = {
                val nextPage = (currentPage + 1).coerceAtMost(maxPage)
                onNextPage(nextPage)
            }) {
                Icon(painter = AppIcon.ChevronRight.getPainter(), contentDescription = "next page")
            }
        }
    }
}