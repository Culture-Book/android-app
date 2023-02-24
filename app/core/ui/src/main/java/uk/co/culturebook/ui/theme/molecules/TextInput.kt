package uk.co.culturebook.ui.theme.molecules

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.*


fun String.isNotValidEmail() =
    !Regex("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+))")
        .matches(this)

fun String.isNotValidPassword() = !Regex("^(?=.*\\d)(?=.*[a-z])(?=.*[a-zA-Z]).{8,}\$").matches(this)

fun String.isValidEmail() = !isNotValidEmail()

private val String.isNotValidEmailValue get() = isNotValidEmail() && isNotBlank()

private val String.isNotValidPasswordValue get() = isNotValidPassword() && isNotBlank()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailField(value: String, onValueChanged: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = smallSize),
        value = value,
        onValueChange = onValueChanged,
        shape = mediumRoundedShape,
        label = { Text(stringResource(R.string.email)) },
        isError = value.isNotValidEmailValue,
        supportingText = {
            if (value.isNotValidEmailValue) Text(stringResource(R.string.email_invalid))
        },
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
            .padding(vertical = smallSize),
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
        .padding(vertical = smallSize),
        value = value,
        onValueChange = onValueChanged,
        isError = value != confirmPassword || value.isNotValidPasswordValue,
        supportingText = {
            Column {
                if (value != confirmPassword) Text(stringResource(R.string.password_match))
                if (value.isNotValidPasswordValue) Text(stringResource(R.string.password_invalid))
            }
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
        .padding(vertical = smallSize),
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
            .padding(vertical = smallSize),
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
            .padding(vertical = smallSize)
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun LargeDynamicRoundedTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    readOnly: Boolean = false
) {
    var expand by remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = modifier
            .animateContentSize()
    ) {
        val height = if (expand) maxHeight else minHeight
        TextField(
            modifier = Modifier
                .defaultMinSize(minHeight = xxxxlSize)
                .onFocusChanged {
                    if (it.isFocused) {
                        expand = true
                    }
                }
                .height(height)
                .fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            value = value,
            shape = mediumRoundedShape,
            onValueChange = onValueChange,
            readOnly = readOnly
        )

        if (maxHeight != minHeight) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(mediumSize),
                onClick = { expand = !expand }) {
                if (expand) Icon(
                    painter = AppIcon.ChevronUp.getPainter(),
                    contentDescription = "expand button"
                ) else Icon(
                    painter = AppIcon.ChevronDown.getPainter(),
                    contentDescription = "expand button"
                )
            }
        }
    }
}