package io.culturebook.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import io.culturebook.ui.R

object AppIcons {
    val filledDateRange = Icons.Filled.DateRange
    val outlinedDateRange = Icons.Outlined.DateRange
    val filledSettings = Icons.Filled.Settings
    val outlinedSettings = Icons.Outlined.Settings
    val logo = R.drawable.ic_logo
    val chevron_down = Icons.Filled.ArrowDropDown
    val chevron_left = Icons.Filled.KeyboardArrowLeft
    val chevron_right = Icons.Filled.KeyboardArrowRight
    val add = Icons.Default.Add
    val tick = Icons.Default.Check
    val visibility = R.drawable.ic_outline_visibility
    val visibility_off = R.drawable.ic_outline_visibility_off

    @Composable
    fun Int.getPainter() = painterResource(this)

    @Composable
    fun ImageVector.getPainter() = rememberVectorPainter(this)
}
