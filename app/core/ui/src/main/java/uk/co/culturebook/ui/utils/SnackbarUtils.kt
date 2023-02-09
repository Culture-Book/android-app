package uk.co.culturebook.ui.utils

import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ShowSnackbar(
    @StringRes stringId: Int,
    snackbarState: SnackbarHostState,
    onShow: () -> Unit = {},
    coroutine: CoroutineScope? = null
) {
    val string = stringResource(stringId)
    LaunchedEffect(Unit) {
        /**
         * We need the remembered coroutine scope in here.
         * This is because when the result of the addFilesLauncher returns,
         * the Launched effect cancels the coroutine scope. Therefore,
         * we need something to remember the coroutine scope outside of the composition.
         * */
        coroutine?.launch {
            snackbarState.showSnackbar(string)
            onShow()
        } ?: run {
            snackbarState.showSnackbar(string)
            onShow()
        }
    }
}