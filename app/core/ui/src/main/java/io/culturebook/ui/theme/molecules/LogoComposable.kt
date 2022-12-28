package io.culturebook.ui.theme.molecules

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import io.culturebook.ui.R

@Composable
fun LogoComposable(modifier: Modifier) {
    Icon(
        modifier = modifier,
        painter = painterResource(id = R.drawable.ic_logo),
        contentDescription = "logo"
    )
}