package uk.co.culturebook.ui.theme.molecules

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import uk.co.culturebook.ui.R

@Composable
fun LogoComposable(modifier: Modifier) {
    Icon(
        modifier = modifier,
        painter = painterResource(id = R.drawable.ic_logo),
        contentDescription = "logo",
        tint = Color.Unspecified
    )
}