package uk.co.culturebook.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

val xxsSize = 1.dp
val xsSize = 4.dp
val smallSize = 8.dp
val mediumSize = 16.dp
val largeSize = 24.dp
val xlSize = 48.dp
val xxlSize = 64.dp
val xxxlSize = 96.dp
val xxxxlSize = 125.dp
val fiveXlSize = 145.dp

const val MinScreenWidth = 350

val smallRoundedShape = RoundedCornerShape(smallSize)
val mediumRoundedShape = RoundedCornerShape(mediumSize)
val mediumHeaderRoundedShape = RoundedCornerShape(topStart = mediumSize, topEnd = mediumSize)
val mediumFooterRoundedShape = RoundedCornerShape(bottomStart = mediumSize, bottomEnd = mediumSize)
val largeRoundedShape = RoundedCornerShape(largeSize)

// Buttons
val buttonHeader = RoundedCornerShape(
    topStart = mediumSize,
    bottomStart = mediumSize,
    topEnd = xsSize,
    bottomEnd = xsSize
)

val buttonInside = RoundedCornerShape(
    topStart = xsSize,
    bottomStart = xsSize,
    topEnd = xsSize,
    bottomEnd = xsSize
)

val buttonFooter = RoundedCornerShape(
    topStart = xsSize,
    bottomStart = xsSize,
    topEnd = mediumSize,
    bottomEnd = mediumSize
)