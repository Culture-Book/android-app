package uk.co.culturebook.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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

    object Close : AppIcon {
        private val icon = Icons.Default.Close

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

    object Add : AppIcon {
        @Composable
        override fun getPainter() = Icons.Default.Add.getPainter()
    }

    object Pin : AppIcon {

        @Composable
        override fun getPainter() = Icons.Default.LocationOn.getPainter()
    }

    object MyLocation : AppIcon {
        private val icon = R.drawable.my_location

        @Composable
        override fun getPainter() = icon.getPainter()
    }

    object LocationOff : AppIcon {
        private val icon = R.drawable.location_disabled

        @Composable
        override fun getPainter() = icon.getPainter()
    }

    object LocationSearching : AppIcon {
        private val icon = R.drawable.location_searching

        @Composable
        override fun getPainter() = icon.getPainter()
    }

    object Tick : AppIcon {
        @Composable
        override fun getPainter() = Icons.Default.Check.getPainter()
    }
}

@Composable
private fun Int.getPainter() = painterResource(this)

@Composable
private fun ImageVector.getPainter() = rememberVectorPainter(this)
