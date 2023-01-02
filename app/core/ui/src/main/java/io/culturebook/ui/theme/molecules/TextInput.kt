package io.culturebook.ui.theme.molecules

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import io.culturebook.ui.R
import io.culturebook.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailField(value: String, onValueChanged: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = smallPadding),
        value = value,
        onValueChange = onValueChanged,
        shape = mediumRoundedShape,
        label = { Text(stringResource(R.string.email)) },
        singleLine = true,
        keyboardOptions = defaultEmailKeyboardOptions,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayNameField(value: String, onValueChanged: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = smallPadding),
        value = value,
        onValueChange = onValueChanged,
        shape = mediumRoundedShape,
        label = { Text(stringResource(R.string.display_name)) },
        singleLine = true,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(value: String, confirmPassword: String, onValueChanged: (String) -> Unit) {
    var showPassword by remember { mutableStateOf(false) }
    OutlinedTextField(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = smallPadding),
        value = value,
        onValueChange = onValueChanged,
        isError = value != confirmPassword,
        supportingText = {
            if (value != confirmPassword) Text(stringResource(R.string.password_match))
        },
        shape = mediumRoundedShape,
        label = { Text(stringResource(R.string.password)) },
        singleLine = true,
        visualTransformation = if (!showPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = defaultPasswordKeyboardOptions,
        trailingIcon = {
            IconButton(onClick = { showPassword = !showPassword }) {
                Icon(
                    painter = if (!showPassword) AppIcon.Visibility.getPainter() else AppIcon.VisibilityOff.getPainter(),
                    contentDescription = "password_visibility"
                )
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmPasswordField(value: String, password: String, onValueChanged: (String) -> Unit) {
    var showConfirmPassword by remember { mutableStateOf(false) }
    OutlinedTextField(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = smallPadding),
        value = value,
        onValueChange = onValueChanged,
        shape = mediumRoundedShape,
        label = { Text(stringResource(R.string.confirm_password)) },
        singleLine = true,
        isError = password != value,
        supportingText = {
            if (password != value) Text(stringResource(R.string.password_match))
        },
        visualTransformation = if (!showConfirmPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = defaultPasswordKeyboardOptions,
        trailingIcon = {
            IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                Icon(
                    painter = if (!showConfirmPassword) AppIcon.Visibility.getPainter() else AppIcon.VisibilityOff.getPainter(),
                    contentDescription = "password_visibility"
                )
            }
        })

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginEmailField(value: String, onValueChanged: (String) -> Unit, onNext: (Boolean) -> Unit) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = smallPadding),
        value = value,
        onValueChange = onValueChanged,
        shape = mediumRoundedShape,
        label = { Text(stringResource(R.string.email)) },
        singleLine = true,
        keyboardOptions = emailKeyboardOptions,
        keyboardActions = KeyboardActions(onNext = { onNext(true) })
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPasswordField(
    value: String,
    focusRequester: FocusRequester,
    onValueChanged: (String) -> Unit,
    onDone: () -> Unit
) {
    var showPassword by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = smallPadding)
            .focusRequester(focusRequester),
        value = value,
        onValueChange = onValueChanged,
        shape = mediumRoundedShape,
        label = { Text(stringResource(R.string.password)) },
        singleLine = true,
        visualTransformation = if (!showPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = passwordKeyboardOptions,
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        trailingIcon = {
            IconButton(onClick = { showPassword = !showPassword }) {
                Icon(
                    painter = if (!showPassword) AppIcon.Visibility.getPainter() else AppIcon.VisibilityOff.getPainter(),
                    contentDescription = "password_visibility"
                )
            }
        }
    )
}