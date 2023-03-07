package uk.co.culturebook.ui.utils

import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import uk.co.culturebook.ui.theme.mediumRoundedShape
import uk.co.culturebook.ui.theme.xxsSize

@Composable
fun Modifier.borderIf(
    width: Dp = xxsSize,
    color: Color = MaterialTheme.colorScheme.outline,
    shape: Shape = mediumRoundedShape,
    block: () -> Boolean
): Modifier {
    return if (block()) {
        then(Modifier.border(width, color, shape))
    } else {
        this
    }
}