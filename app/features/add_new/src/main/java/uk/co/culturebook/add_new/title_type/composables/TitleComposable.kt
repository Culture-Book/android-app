package uk.co.culturebook.add_new.title_type.composables

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.mediumRoundedShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleComposable(
    modifier: Modifier = Modifier,
    title: String,
    isError: Boolean,
    onTitleChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        shape = mediumRoundedShape,
        value = title,
        isError = isError && title.isNotBlank(),
        onValueChange = onTitleChange,
        label = { Text(stringResource(R.string.name)) })
}