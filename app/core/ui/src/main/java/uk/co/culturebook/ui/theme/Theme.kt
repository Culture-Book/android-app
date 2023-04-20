package uk.co.culturebook.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlin.math.ln


private val LightColors = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
)


private val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
)

@Composable
fun AppTheme(
    isMaterialYou: Boolean = false,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val colors = when {
        isMaterialYou && useDarkTheme -> dynamicDarkColorScheme(context)
        isMaterialYou && !useDarkTheme -> dynamicLightColorScheme(context)
        useDarkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = {
            SystemBarsColors(
                navigationColor = MaterialTheme.colorScheme.background,
                statusBarColor = MaterialTheme.colorScheme.background
            )
            content()
        }
    )
}

@Composable
fun SystemBarsColors(
    navigationColor: Color? = null,
    statusBarColor: Color? = null
) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    LaunchedEffect(systemUiController, useDarkIcons, navigationColor, statusBarColor) {
        if (navigationColor != null) {
            systemUiController.setNavigationBarColor(navigationColor)
        }
        if (statusBarColor != null) {
            systemUiController.setStatusBarColor(statusBarColor)
        }
    }
}

@Composable
fun surfaceColorAtElevation(color: Color, elevation: Dp): Color {
    return if (color == MaterialTheme.colorScheme.surface) {
        MaterialTheme.colorScheme.surfaceColorAtElevation(elevation)
    } else {
        color.colorAtElevation(
            elevation = elevation,
            colorTint = MaterialTheme.colorScheme.surfaceTint
        )
    }
}

fun Color.colorAtElevation(elevation: Dp, colorTint: Color): Color {
    if (elevation == 0.dp) return this
    val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
    return colorTint.copy(alpha = alpha).compositeOver(this)
}