package uk.co.culturebook.home.explore

import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppIcon
import uk.co.culturebook.ui.theme.largeRoundedShape
import uk.co.culturebook.ui.theme.smallSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun SearchAppBar(
    modifier: Modifier = Modifier,
    onFiltersClicked: () -> Unit = {},
    onSearchClicked: (String) -> Unit = {}
) {
    var searchString by remember { mutableStateOf("") }

    TopAppBar(
        modifier = modifier,
        windowInsets = WindowInsets.statusBars,
        title = {
            SearchField(value = searchString) { searchString = it }
        },
        actions = {
            if (searchString.isNotBlank()) {
                IconButton(onClick = { onSearchClicked(searchString) }) {
                    Icon(
                        painter = AppIcon.Tick.getPainter(),
                        contentDescription = "search icon"
                    )
                }
            }
            IconButton(onClick = { onFiltersClicked() }) {
                Icon(
                    painter = AppIcon.Filter.getPainter(),
                    contentDescription = "search icon"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun SearchField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChanged: (String) -> Unit = {}
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            containerColor = Color.Transparent
        ),
        leadingIcon = {
            if (value.isEmpty()) {
                Icon(
                    painter = AppIcon.Search.getPainter(),
                    contentDescription = "search icon"
                )
            } else {
                IconButton(onClick = { onValueChanged("") }) {
                    Icon(
                        painter = AppIcon.Close.getPainter(),
                        contentDescription = "search icon"
                    )
                }
            }
        },
        placeholder = {
            Text(stringResource(R.string.search_filter))
        },
        singleLine = true,
        value = value,
        shape = largeRoundedShape,
        onValueChange = onValueChanged
    )
}