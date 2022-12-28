package io.culturebook.ui.theme

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType

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