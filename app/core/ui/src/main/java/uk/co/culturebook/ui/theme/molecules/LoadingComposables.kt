package uk.co.culturebook.ui.theme.molecules

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import uk.co.culturebook.ui.theme.largeSize

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoadingComposable(padding: PaddingValues = PaddingValues(largeSize)) {
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(keyboardController) {
        keyboardController?.hide()
    }

    BoxWithConstraints(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
    ) { CircularProgressIndicator(modifier = Modifier.align(Alignment.Center)) }

}