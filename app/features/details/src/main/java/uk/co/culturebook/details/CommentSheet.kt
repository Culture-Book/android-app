package uk.co.culturebook.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uk.co.common.BlockOptionsState
import uk.co.culturebook.data.models.cultural.Comment
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.*
import uk.co.culturebook.ui.theme.molecules.ModalBottomSheet
import uk.co.culturebook.ui.theme.molecules.SecondarySurfaceWithIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentSheet(
    onDismiss: () -> Unit,
    listComments: List<Comment>,
    onCommentSent: (String) -> Unit,
    onOptionSelected: (BlockOptionsState) -> Unit
) {
    var comment by remember { mutableStateOf("") }
    val localConfig = LocalConfiguration.current

    ModalBottomSheet(
        onDismiss = onDismiss,
        footer = {
            OutlinedTextField(
                modifier = Modifier
                    .height(xxxlSize)
                    .fillMaxWidth(),
                value = comment,
                onValueChange = { comment = it },
                shape = mediumRoundedShape,
                label = {
                    Text(stringResource(R.string.add_comment))
                },
                trailingIcon = {
                    IconButton(
                        onClick = { if (comment.isNotBlank()) onCommentSent(comment) },
                        enabled = comment.isNotBlank()
                    ) {
                        Icon(
                            painter = AppIcon.Send.getPainter(),
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = "send comment"
                        )
                    }
                }
            )
        })
    {
        LazyColumn(
            modifier = Modifier.heightIn(
                min = localConfig.screenHeightDp.dp * 0.3f,
                max = localConfig.screenHeightDp.dp * 0.9f
            )
        ) {
            if (listComments.isEmpty()) {
                item { Text(text = stringResource(id = R.string.no_comments)) }
            } else {
                items(listComments) {
                    var expanded by remember { mutableStateOf(false) }
                    SecondarySurfaceWithIcon(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = smallSize),
                        title = it.comment,
                        icon = {
                            Icon(
                                painter = AppIcon.MoreVert.getPainter(),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                contentDescription = "block comment"
                            )

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }) {
                                DropdownMenuItem(onClick = {
                                    expanded = false
                                    onOptionSelected(BlockOptionsState.Hide(id = it.id))
                                }, text = { Text(stringResource(R.string.hide)) })
                                DropdownMenuItem(onClick = {
                                    expanded = false
                                    onOptionSelected(BlockOptionsState.Report(id = it.id))
                                }, text = { Text(stringResource(R.string.report)) })
                                DropdownMenuItem(onClick = {
                                    expanded = false
                                    onOptionSelected(BlockOptionsState.Block(id = it.id))
                                }, text = { Text(stringResource(R.string.block)) })
                            }

                        },
                        onButtonClicked = {
                            expanded = true
                        }
                    )
                }
            }
        }
    }
}