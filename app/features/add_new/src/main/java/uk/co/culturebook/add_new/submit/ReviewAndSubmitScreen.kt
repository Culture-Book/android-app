package uk.co.culturebook.add_new.submit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import uk.co.culturebook.add_new.data.ElementState
import uk.co.culturebook.add_new.title_type.composables.icon
import uk.co.culturebook.add_new.title_type.composables.label
import uk.co.culturebook.data.models.cultural.ElementType
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppIcon
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.molecules.*
import uk.co.culturebook.ui.theme.smallSize
import uk.co.culturebook.ui.theme.xxxxlSize
import uk.co.culturebook.ui.utils.ShowSnackbar
import uk.co.culturebook.ui.utils.prettyPrint
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitScreenComposable(
    onBack: () -> Unit,
    elementState: ElementState,
    state: SubmitState,
    onSubmit: (ElementState) -> Unit
) {
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    if (state is SubmitState.Error) ShowSnackbar(
        stringId = R.string.generic_sorry,
        snackbarState = snackbarHostState
    )

    when (state) {
        SubmitState.Loading -> LoadingComposable()
        SubmitState.Error,
        SubmitState.Idle,
        SubmitState.Success ->
            Scaffold(
                topBar = { ReviewAppbar(onBack) },
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(horizontal = mediumSize)
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                    horizontalAlignment = CenterHorizontally
                ) {

                    TitleAndSubtitle(
                        title = elementState.name,
                        titleContent = {
                            elementState.type?.let {
                                Icon(
                                    painter = it.icon,
                                    contentDescription = it.label
                                )
                            }
                        }
                    )

                    if (elementState.files.isNotEmpty()) {
                        ImageWithIcon(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(xxxxlSize * 2)
                                .padding(mediumSize),
                            uri = elementState.files.first().uri
                        )
                    }

                    TitleAndSubtitle(title = stringResource(R.string.background))

                    LargeDynamicRoundedTextField(
                        modifier = Modifier
                            .defaultMinSize(minHeight = xxxxlSize)
                            .padding(vertical = mediumSize)
                            .fillMaxWidth(),
                        value = elementState.information,
                        readOnly = true
                    )

                    if (elementState.type == ElementType.Event) {
                        TitleAndSubtitle(
                            modifier = Modifier.padding(vertical = smallSize),
                            title = stringResource(R.string.event_info)
                        )

                        if (elementState.eventType?.startDateTime != null) {
                            OutlinedSurface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = smallSize),
                                title = stringResource(
                                    id = R.string.event_date,
                                    elementState.eventType?.startDateTime?.prettyPrint() ?: ""
                                )
                            )
                        }

                        if (elementState.eventType?.location != null) {
                            OutlinedSurface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = smallSize),
                                title = stringResource(
                                    id = R.string.event_location,
                                    "${elementState.eventType?.location?.latitude?.roundToInt()} - ${elementState.eventType?.location?.longitude?.roundToInt()}"
                                )
                            )
                        }
                    }

                    if (elementState.files.isNotEmpty()) {
                        TitleAndSubtitle(
                            modifier = Modifier.padding(vertical = mediumSize),
                            title = stringResource(R.string.media)
                        )

                        LazyRow {
                            items(elementState.files) {
                                ImageWithIcon(
                                    modifier = Modifier
                                        .size(height = xxxxlSize, width = xxxxlSize * 1.5f)
                                        .padding(end = mediumSize),
                                    uri = it.uri
                                )
                            }
                        }
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = mediumSize),
                        onClick = { onSubmit(elementState) }) {
                        Text(stringResource(R.string.submit))
                    }
                }
            }
    }


}

@Composable
fun ElementType.getString() = stringResource(
    when (this) {
        ElementType.Food -> R.string.food
        ElementType.Music -> R.string.music
        ElementType.Story -> R.string.story
        ElementType.PoI -> R.string.poi
        ElementType.Event -> R.string.event
    }
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewAppbar(navigateBack: () -> Unit = {}) {
    TopAppBar(modifier = Modifier.fillMaxWidth(),
        title = { Text(stringResource(R.string.review_submit)) },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    painter = AppIcon.ArrowBack.getPainter(), contentDescription = "navigate back"
                )
            }
        })
}