package uk.co.culturebook.ui.theme

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.google.maps.android.compose.MapUiSettings

val emailKeyboardOptions = KeyboardOptions(
    capitalization = KeyboardCapitalization.None,
    autoCorrect = false,
    keyboardType = KeyboardType.Email,
    imeAction = ImeAction.Next
)

val passwordKeyboardOptions = KeyboardOptions(
    capitalization = KeyboardCapitalization.None,
    autoCorrect = false,
    keyboardType = KeyboardType.Password,
    imeAction = ImeAction.Done
)
val defaultEmailKeyboardOptions = KeyboardOptions(
    capitalization = KeyboardCapitalization.None,
    autoCorrect = false,
    keyboardType = KeyboardType.Email,
)

val defaultPasswordKeyboardOptions = KeyboardOptions(
    capitalization = KeyboardCapitalization.None,
    autoCorrect = false,
    keyboardType = KeyboardType.Password,
)

val mapUiSettings = MapUiSettings(
    compassEnabled = false,
    myLocationButtonEnabled = true,
    zoomControlsEnabled = false
)