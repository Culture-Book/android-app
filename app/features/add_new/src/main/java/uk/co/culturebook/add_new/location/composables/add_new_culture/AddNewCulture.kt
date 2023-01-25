package uk.co.culturebook.add_new.location.composables.add_new_culture

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import uk.co.culturebook.add_new.location.events.LocationEvent
import uk.co.culturebook.data.models.cultural.Location
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.mediumPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewCulture(
    modifier: Modifier = Modifier,
    location: Location,
    postEvent: (LocationEvent) -> Unit
) {
    var cultureName by remember { mutableStateOf("") }
    val isError by remember { derivedStateOf { !cultureName.matches(Regex("[a-zA-Z ]+")) } }

    Column(modifier = modifier) {

        Text(
            text = stringResource(R.string.add_culture_title),
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            modifier = Modifier.padding(vertical = mediumPadding),
            text = stringResource(R.string.add_culture_message),
            style = MaterialTheme.typography.bodyMedium
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = cultureName,
            isError = isError && cultureName.isNotBlank(),
            onValueChange = { cultureName = it },
            label = { Text(stringResource(R.string.culture_name)) })

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = mediumPadding)
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !isError && cultureName.isNotBlank(),
                onClick = { postEvent(LocationEvent.AddCulture(cultureName.trim(), location)) }) {
                Text(stringResource(R.string.add_new_culture))
            }

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { postEvent(LocationEvent.ShowMap) }) {
                Text(stringResource(R.string.select_location))
            }
        }
    }
}