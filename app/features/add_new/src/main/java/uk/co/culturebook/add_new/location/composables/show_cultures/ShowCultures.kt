package uk.co.culturebook.add_new.location.composables.show_cultures

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uk.co.culturebook.data.models.cultural.Culture
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowCultures(
    modifier: Modifier = Modifier,
    cultures: List<Culture> = emptyList(),
    onCultureSelected: (Culture) -> Unit,
    onAddNewCultureClicked: () -> Unit,
    onSelectLocation: () -> Unit
) {
    var selectedCulture by remember { mutableStateOf<Culture?>(null) }
    Column(modifier = modifier) {

        Text(
            text = stringResource(R.string.select_culture),
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            modifier = Modifier.padding(vertical = mediumPadding),
            text = stringResource(R.string.select_culture_message),
            style = MaterialTheme.typography.bodyMedium
        )

        LazyColumn {
            items(cultures.size) {
                val culture = cultures[it]
                val selectedColor =
                    if (selectedCulture?.id == culture.id) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = smallPadding),
                    color = selectedColor,
                    tonalElevation = smallPadding,
                    shape = smallRoundedShape,
                    onClick = { selectedCulture = culture })
                {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(modifier = Modifier.padding(16.dp), text = culture.name)
                        if (selectedCulture?.id == culture.id) {
                            Icon(
                                modifier = Modifier.padding(16.dp),
                                painter = AppIcon.Tick.getPainter(),
                                contentDescription = "Tick"
                            )
                        }
                    }
                }
            }

            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    tonalElevation = smallPadding,
                    shape = smallRoundedShape,
                    onClick = { onAddNewCultureClicked() })
                {
                    Row {
                        Icon(
                            modifier = Modifier.padding(16.dp),
                            painter = AppIcon.Add.getPainter(),
                            contentDescription = "Add"
                        )
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = stringResource(R.string.add_new_culture)
                        )
                    }

                }
            }

            item {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = smallPadding),
                    enabled = selectedCulture != null,
                    onClick = { selectedCulture?.let { onCultureSelected(it) } }
                ) {
                    Text(stringResource(R.string.select_culture))
                }
            }

            item {
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = { onSelectLocation() }
                ) {
                    Text(stringResource(R.string.select_location))
                }
            }
        }
    }

}