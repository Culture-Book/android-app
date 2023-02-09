package uk.co.culturebook.add_new.info.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.smallSize

@Composable
fun EventTypeComposable(
    onDateClicked: () -> Unit,
    onLocation: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.5f)
                .padding(end = smallSize),
            onClick = { onDateClicked() }) {
            Text(stringResource(R.string.select_date))
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.5f),
            onClick = { onLocation() }) {
            Text(stringResource(R.string.select_location))
        }
    }
}