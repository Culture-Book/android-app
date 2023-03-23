package uk.co.culturebook.explore

import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import uk.co.culturebook.data.models.cultural.SearchType
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppIcon
import uk.co.culturebook.ui.theme.largeRoundedShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun SearchAppBar(
    modifier: Modifier = Modifier,
    searchString: String = "",
    searchType: SearchType = SearchType.Element,
    onSearchType: (SearchType) -> Unit = {},
    onFiltersClicked: () -> Unit = {},
    onSearchClicked: (String) -> Unit = {}
) {
    var _searchString by remember { mutableStateOf(searchString) }
    TopAppBar(
        windowInsets = WindowInsets.statusBars,
        title = {
            SearchField(value = _searchString) { _searchString = it }
        },
        actions = {
            if (_searchString.isNotBlank()) {
                IconButton(onClick = { onSearchClicked(_searchString) }) {
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
            containerColor = Color.Transparent,
            textColor = MaterialTheme.colorScheme.onSurface
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
            Text(
                stringResource(R.string.search_filter),
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        singleLine = true,
        value = value,
        shape = largeRoundedShape,
        onValueChange = onValueChanged
    )
}