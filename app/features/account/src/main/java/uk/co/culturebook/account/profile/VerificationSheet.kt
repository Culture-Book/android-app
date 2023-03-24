package uk.co.culturebook.account.profile

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppIcon
import uk.co.culturebook.ui.theme.mediumRoundedShape
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.molecules.ModalBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationRequestSheet(
    onDismiss: () -> Unit,
    onVerificationRequested: (String) -> Unit
) {
    var reason by remember { mutableStateOf("") }
    var showNoReason by remember { mutableStateOf(false) }

    if (showNoReason) {
        Toast.makeText(
            LocalContext.current,
            stringResource(R.string.verification_reason_empty),
            Toast.LENGTH_LONG
        ).show()
        showNoReason = false
    }

    ModalBottomSheet(onDismiss = onDismiss, onConfirm = {
        if (reason.isNotEmpty()) {
            onVerificationRequested(reason)
            onDismiss()
        } else {
            showNoReason = true
        }
    }) {
        Icon(
            modifier = Modifier
                .fillMaxWidth()
                .padding(mediumSize),
            painter = AppIcon.Verify.getPainter(),
            contentDescription = "verified icon"
        )
        Text(text = stringResource(R.string.verified_text))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = mediumSize),
            shape = mediumRoundedShape,
            value = reason,
            onValueChange = { reason = it },
            label = { Text(text = stringResource(R.string.verification_reason)) },
        )
    }
}