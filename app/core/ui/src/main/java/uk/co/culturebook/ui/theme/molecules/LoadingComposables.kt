package uk.co.culturebook.ui.theme.molecules

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import uk.co.culturebook.ui.theme.largeSize

@Composable
fun LoadingComposable(padding: PaddingValues = PaddingValues(largeSize)) {
    BoxWithConstraints(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
    ) { CircularProgressIndicator(modifier = Modifier.align(Alignment.Center)) }

}