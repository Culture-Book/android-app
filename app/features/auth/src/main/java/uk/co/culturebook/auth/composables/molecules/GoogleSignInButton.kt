package uk.co.culturebook.auth.composables.molecules

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import uk.co.culturebook.ui.R

@Composable
@Preview
fun GoogleSignInButton(
    modifier: Modifier = Modifier,
    handleSignInResult: (Task<GoogleSignInAccount>) -> Unit = {}
) {
    fun getGoogleClient(context: Context): GoogleSignInClient {
        return GoogleSignIn.getClient(
            context, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestProfile()
                .build()
        )
    }

    val activityResult = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val intent = result.data
        if (intent != null) {
            GoogleSignIn.getSignedInAccountFromIntent(intent)
                .addOnCompleteListener { handleSignInResult(it) }
        }
    }

    val context = LocalContext.current

    OutlinedButton(
        modifier = modifier,
        onClick = { getGoogleClient(context).also { activityResult.launch(it.signInIntent) } }) {
        Text(stringResource(R.string.continue_with_google))
    }
}