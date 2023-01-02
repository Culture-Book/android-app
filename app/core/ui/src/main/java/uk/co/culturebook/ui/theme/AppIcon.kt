package uk.co.culturebook.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import uk.co.culturebook.ui.R

sealed interface AppIcon {
    @Composable
    fun getPainter(): Painter

    object ArrowBack : AppIcon {
        private val icon = Icons.Default.ArrowBack

        @Composable
        override fun getPainter() = icon.getPainter()
    }

    object Visibility : AppIcon {
        private val icon = R.drawable.ic_outline_visibility

        @Composable
        override fun getPainter() = icon.getPainter()
    }

    object VisibilityOff : AppIcon {
        private val icon = R.drawable.ic_outline_visibility_off

        @Composable
        override fun getPainter() = icon.getPainter()
    }

    object MagnifyGlass : AppIcon {
        private val icon = Icons.Filled.Search

        @Composable
        override fun getPainter() = icon.getPainter()
    }

    object MagnifyGlassOutline : AppIcon {
        private val icon = Icons.Outlined.Search

        @Composable
        override fun getPainter() = icon.getPainter()
    }

    object Account : AppIcon {
        private val icon = Icons.Filled.Person

        @Composable
        override fun getPainter() = icon.getPainter()
    }

    object AccountOutline : AppIcon {
        private val icon = Icons.Outlined.Person

        @Composable
        override fun getPainter() = icon.getPainter()
    }

    object Social : AppIcon {
        private val icon = R.drawable.ic_groups

        @Composable
        override fun getPainter() = icon.getPainter()
    }

    object SocialOutline : AppIcon {
        private val icon = R.drawable.ic_outline_groups

        @Composable
        override fun getPainter() = icon.getPainter()
    }

}

@Composable
private fun Int.getPainter() = painterResource(this)

@Composable
private fun ImageVector.getPainter() = rememberVectorPainter(this)
