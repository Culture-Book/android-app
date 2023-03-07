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

    object Tick : AppIcon {
        @Composable
        override fun getPainter() = Icons.Default.Check.getPainter()
    }

    object Food : AppIcon {
        private val icon = R.drawable.ic_restaurant

        @Composable
        override fun getPainter() = icon.getPainter()
    }

    object Music : AppIcon {
        private val icon = R.drawable.ic_music

        @Composable
        override fun getPainter() = icon.getPainter()
    }

    object Story : AppIcon {
        private val icon = R.drawable.ic_book

        @Composable
        override fun getPainter() = icon.getPainter()
    }

    object PointOfInterest : AppIcon {
        private val icon = R.drawable.ic_pin_drop

        @Composable
        override fun getPainter() = icon.getPainter()
    }

    object Event : AppIcon {
        private val icon = R.drawable.ic_event

        @Composable
        override fun getPainter() = icon.getPainter()
    }

    object Bin : AppIcon {
        @Composable
        override fun getPainter() = Icons.Default.Delete.getPainter()
    }

    object ChevronDown : AppIcon {
        @Composable
        override fun getPainter() = Icons.Default.ArrowDropDown.getPainter()
    }

    object ChevronLeft : AppIcon {
        @Composable
        override fun getPainter() = Icons.Default.KeyboardArrowLeft.getPainter()
    }

    object ChevronRight : AppIcon {
        @Composable
        override fun getPainter() = Icons.Default.KeyboardArrowRight.getPainter()
    }

    object ChevronUp : AppIcon {
        private val icon = R.drawable.ic_drop_up

        @Composable
        override fun getPainter() = icon.getPainter()
    }

    object BrokenImage : AppIcon {
        val icon = R.drawable.ic_broken_image

        @Composable
        override fun getPainter() = icon.getPainter()
    }

    object Audio : AppIcon {
        private val icon = R.drawable.ic_music_note

        @Composable
        override fun getPainter() = icon.getPainter()
    }

    object Info : AppIcon {
        @Composable
        override fun getPainter() = Icons.Default.Info.getPainter()
    }

    object Filter : AppIcon {
        @Composable
        override fun getPainter() = R.drawable.ic_filter.getPainter()
    }

    object Search : AppIcon {
        @Composable
        override fun getPainter() = Icons.Default.Search.getPainter()
    }

    object Castle : AppIcon {
        @Composable
        override fun getPainter() = R.drawable.ic_castle.getPainter()
    }

    object FavouriteOutline : AppIcon {
        @Composable
        override fun getPainter() = R.drawable.ic_star_outline.getPainter()
    }

    object FavouriteFilled : AppIcon {
        @Composable
        override fun getPainter() = Icons.Filled.Star.getPainter()
    }
}

@Composable
private fun Int.getPainter() = painterResource(this)

@Composable
private fun ImageVector.getPainter() = rememberVectorPainter(this)
