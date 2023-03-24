package uk.co.culturebook.account.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uk.co.common.TypeFilters
import uk.co.culturebook.data.models.cultural.BlockedContribution
import uk.co.culturebook.data.models.cultural.BlockedCulture
import uk.co.culturebook.data.models.cultural.BlockedElement
import uk.co.culturebook.data.models.cultural.SearchType
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppIcon
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.molecules.ModalBottomSheet
import uk.co.culturebook.ui.theme.molecules.SecondarySurfaceWithIcon
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockedElementsSheet(
    onDismiss: () -> Unit,
    listOfElements: List<BlockedElement> = emptyList(),
    listOfContributions: List<BlockedContribution> = emptyList(),
    listOfCultures: List<BlockedCulture> = emptyList(),
    onUnblock: (UUID, SearchType) -> Unit
) {
    var type by remember { mutableStateOf<SearchType>(SearchType.Element) }

    ModalBottomSheet(
        onDismiss = onDismiss,
        footer = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                TextButton(onClick = { onDismiss() }) {
                    Text(stringResource(R.string.close))
                }
            }
        }
    ) {
        TypeFilters(
            modifier = Modifier
                .padding(vertical = mediumSize)
                .fillMaxWidth(),
            type = type,
            onTypeChanged = { type = it }
        )

        when (type) {
            SearchType.Element -> BlockedElementsList(
                listOfElements = listOfElements,
                onUnblock = onUnblock
            )
            SearchType.Contribution -> BlockedContributionsList(
                listOfContributions = listOfContributions,
                onUnblock = onUnblock
            )
            SearchType.Culture -> BlockedCulturesList(
                listOfCultures = listOfCultures,
                onUnblock = onUnblock
            )
        }
    }
}

@Composable
fun BlockedElementsList(
    modifier: Modifier = Modifier,
    listOfElements: List<BlockedElement>,
    onUnblock: (UUID, SearchType) -> Unit
) {
    val localConfig = LocalConfiguration.current
    LazyColumn(
        modifier = Modifier.heightIn(
            min = 0.dp,
            max = localConfig.screenHeightDp.dp * .9f
        )
    ) {
        items(listOfElements.size) { index ->
            SecondarySurfaceWithIcon(
                modifier = modifier,
                title = listOfElements[index].name,
                icon = {
                    Icon(
                        modifier = Modifier.padding(mediumSize),
                        painter = AppIcon.Bin.getPainter(),
                        contentDescription = "culture icon"
                    )
                },
                onButtonClicked = { onUnblock(listOfElements[index].uuid, SearchType.Element) }
            )
        }
    }
}

@Composable
fun BlockedContributionsList(
    modifier: Modifier = Modifier,
    listOfContributions: List<BlockedContribution>,
    onUnblock: (UUID, SearchType) -> Unit
) {
    val localConfig = LocalConfiguration.current
    LazyColumn(
        modifier = Modifier.heightIn(
            min = 0.dp,
            max = localConfig.screenHeightDp.dp * .9f
        )
    ) {
        items(listOfContributions.size) { index ->
            SecondarySurfaceWithIcon(
                modifier = modifier,
                title = listOfContributions[index].name,
                icon = {
                    Icon(
                        modifier = Modifier.padding(mediumSize),
                        painter = AppIcon.Bin.getPainter(),
                        contentDescription = "culture icon"
                    )
                },
                onButtonClicked = {
                    onUnblock(
                        listOfContributions[index].uuid,
                        SearchType.Contribution
                    )
                }
            )
        }
    }
}

@Composable
fun BlockedCulturesList(
    modifier: Modifier = Modifier,
    listOfCultures: List<BlockedCulture>,
    onUnblock: (UUID, SearchType) -> Unit
) {
    val localConfig = LocalConfiguration.current
    LazyColumn(
        modifier = Modifier.heightIn(
            min = 0.dp,
            max = localConfig.screenHeightDp.dp * .9f
        )
    ) {
        items(listOfCultures.size) { index ->
            SecondarySurfaceWithIcon(
                modifier = modifier,
                title = listOfCultures[index].name,
                icon = {
                    Icon(
                        modifier = Modifier.padding(mediumSize),
                        painter = AppIcon.Bin.getPainter(),
                        contentDescription = "culture icon"
                    )
                },
                onButtonClicked = { onUnblock(listOfCultures[index].uuid, SearchType.Culture) }
            )
        }
    }
}