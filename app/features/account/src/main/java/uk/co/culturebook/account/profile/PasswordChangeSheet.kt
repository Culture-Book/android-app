package uk.co.culturebook.account.profile

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.molecules.ConfirmPasswordField
import uk.co.culturebook.ui.theme.molecules.ModalBottomSheet
import uk.co.culturebook.ui.theme.molecules.PasswordField
import uk.co.culturebook.ui.theme.molecules.isNotValidPassword

@Composable
fun PasswordChangeSheet(
    onDismiss: () -> Unit,
    onPasswordChange: (String, String) -> Unit
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showError by remember { mutableStateOf(false) }

    if (showError) {
        Toast.makeText(
            LocalContext.current,
            stringResource(R.string.password_invalid),
            Toast.LENGTH_LONG
        ).show()
        showError = false
    }

    ModalBottomSheet(onDismiss = onDismiss,
        onConfirm = {
            showError =
                oldPassword.isEmpty() || newPassword.isNotValidPassword() || confirmPassword.isNotValidPassword()
            if (!showError) {
                onPasswordChange(oldPassword, newPassword)
                onDismiss()
            }
        }) {
        PasswordField(
            value = oldPassword,
            confirmPassword = oldPassword,
            onValueChanged = { oldPassword = it })
        PasswordField(
            value = newPassword,
            confirmPassword = confirmPassword,
            onValueChanged = { newPassword = it })
        ConfirmPasswordField(
            value = confirmPassword,
            password = newPassword,
            onValueChanged = { confirmPassword = it })
    }
}