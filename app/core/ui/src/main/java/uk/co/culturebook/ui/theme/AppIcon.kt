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

    object Emoji : AppIcon {
        @Composable
        override fun getPainter() = R.drawable.ic_emoji.getPainter()
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

    object Calendar : AppIcon {
        @Composable
        override fun getPainter() = R.drawable.ic_calendar.getPainter()
    }

    object Share : AppIcon {
        @Composable
        override fun getPainter() = Icons.Default.Share.getPainter()
    }

    object Comment : AppIcon {
        @Composable
        override fun getPainter() = R.drawable.ic_comment.getPainter()
    }

    object Send : AppIcon {
        @Composable
        override fun getPainter() = R.drawable.ic_send.getPainter()
    }

    object MoreVert : AppIcon {
        @Composable
        override fun getPainter() = Icons.Default.MoreVert.getPainter()
    }

    object Sparkle : AppIcon {
        @Composable
        override fun getPainter() = R.drawable.ic_sparkle.getPainter()
    }

    object AccountCircle : AppIcon {
        val icon = R.drawable.ic_account_circle

        @Composable
        override fun getPainter() = icon.getPainter()
    }

    object Settings : AppIcon {
        @Composable
        override fun getPainter() = Icons.Default.Settings.getPainter()
    }

    object About : AppIcon {
        @Composable
        override fun getPainter() = Icons.Default.Info.getPainter()
    }

    object Logout : AppIcon {
        @Composable
        override fun getPainter() = Icons.Default.ExitToApp.getPainter()
    }

    object Verify : AppIcon {
        @Composable
        override fun getPainter() = R.drawable.verified_user.getPainter()
    }

    object Culture1 : AppIcon {
        val icon = R.drawable.ic_culture_1

        @Composable
        override fun getPainter() = icon.getPainter()
    }

    object Culture2 : AppIcon {
        val icon = R.drawable.ic_culture_2

        @Composable
        override fun getPainter() = icon.getPainter()
    }

    object Culture3 : AppIcon {
        val icon = R.drawable.ic_culture_3

        @Composable
        override fun getPainter() = icon.getPainter()
    }

    object NoElements : AppIcon {
        @Composable
        override fun getPainter() = R.drawable.ic_no_elements.getPainter()
    }

    object Vase : AppIcon {
        @Composable
        override fun getPainter() = R.drawable.ic_vase.getPainter()
    }

    object CultureBook : AppIcon {
        @Composable
        override fun getPainter() = R.drawable.ic_culture_book.getPainter()
    }

    object Wreath : AppIcon {
        @Composable
        override fun getPainter() = R.drawable.ic_wreath.getPainter()
    }

    object ImageErrors : AppIcon {
        val listOfPlaceholders = listOf(
            Culture1.icon, Culture2.icon, Culture3.icon
        )

        @Composable
        override fun getPainter() = listOfPlaceholders.random().getPainter()
    }
}

@Composable
private fun Int.getPainter() = painterResource(this)

@Composable
private fun ImageVector.getPainter() = rememberVectorPainter(this)
