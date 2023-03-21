package uk.co.culturebook.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Popup
import androidx.core.text.HtmlCompat
import androidx.emoji2.text.EmojiCompat
import androidx.emoji2.widget.EmojiTextView
import uk.co.culturebook.data.models.cultural.Reaction
import uk.co.culturebook.ui.theme.*
import uk.co.culturebook.ui.theme.molecules.ModalBottomSheet


@Composable
@Preview
fun EmojiSheet(
    selectedEmoji: String = Emoji.emojis.first(),
    emojis: List<String> = Emoji.emojis,
    onDismiss: () -> Unit = {},
    onEmojiSelected: (String) -> Unit = {}
) {
    val localConfig = LocalConfiguration.current
    val gridSize = MaterialTheme.typography.displayMedium.fontSize
    val emojiSize = MaterialTheme.typography.displaySmall.fontSize.value
    val localDensity = LocalDensity.current

    val textSizeInDp = with(localDensity) { gridSize.toDp() }

    ModalBottomSheet(onDismiss = onDismiss, onConfirm = { }, footer = {}) {
        LazyVerticalGrid(
            modifier = Modifier
                .padding(mediumSize)
                .heightIn(
                    min = localConfig.screenHeightDp.dp * 0.3f,
                    max = localConfig.screenHeightDp.dp * 0.9f
                ),
            columns = GridCells.Adaptive(textSizeInDp),
            contentPadding = PaddingValues(xxsSize)
        ) {
            items(emojis) { emoji ->
                val background = if (emoji == selectedEmoji) surfaceColorAtElevation(
                    MaterialTheme.colorScheme.surfaceVariant, mediumSize
                ) else MaterialTheme.colorScheme.surface

                EmojiComposable(
                    color = background,
                    emoji = emoji,
                    emojiSize = emojiSize,
                    onEmojiSelected = onEmojiSelected
                )
            }
        }
    }
}

@Composable
@Preview
fun EmojiPopUp(
    yOffset: Int = 0,
    emojis: Map<Reaction, Int> = Emoji.emojis.map { Reaction(it) }.associateWith { 0 },
    onDismiss: () -> Unit = {},
    onEmojiSelected: (String) -> Unit = {},
    onAddCustomEmoji: () -> Unit = {},
    onDelete: (String) -> Unit = {}
) {
    val emojiSize = MaterialTheme.typography.bodyLarge.fontSize.value

    Popup(
        alignment = Alignment.TopCenter,
        offset = IntOffset(0, yOffset),
        onDismissRequest = onDismiss
    ) {
        LazyRow(
            modifier = Modifier
                .clip(largeRoundedShape)
                .background(
                    surfaceColorAtElevation(MaterialTheme.colorScheme.surface, mediumSize)
                ),
            contentPadding = PaddingValues(xsSize),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(emojis.keys.toList()) { emoji ->
                val background = if (emoji.isMine) surfaceColorAtElevation(
                    MaterialTheme.colorScheme.surfaceVariant, mediumSize
                ) else surfaceColorAtElevation(
                    MaterialTheme.colorScheme.surface, mediumSize
                )

                EmojiComposable(
                    emoji = emoji.reaction,
                    color = background,
                    emojiSize = emojiSize,
                    onEmojiSelected = { if(emoji.isMine) onDelete(it) else onEmojiSelected(it) },
                    number = emojis[emoji]
                )
            }

            item {
                FilledIconButton(
                    modifier = Modifier.height(largeSize),
                    onClick = onAddCustomEmoji
                ) {
                    Icon(
                        painter = AppIcon.Add.getPainter(),
                        contentDescription = "add custom emoji"
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EmojiComposable(
    modifier: Modifier = Modifier,
    emoji: String,
    emojiSize: Float,
    onEmojiSelected: (String) -> Unit,
    color: Color,
    number: Int? = null
) {
    Surface(
        modifier = modifier,
        color = color,
        shape = largeRoundedShape,
        onClick = { onEmojiSelected(emoji) }
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val textColor = MaterialTheme.colorScheme.onSurface
            if (number != null) {
                Spacer(modifier = Modifier.padding(start = smallSize))
            }
            AndroidView(
                factory = {
                    EmojiTextView(it).apply {
                        text = EmojiCompat.get().process(
                            HtmlCompat.fromHtml(
                                emoji, HtmlCompat.FROM_HTML_MODE_LEGACY
                            )
                        )
                        setTextColor(textColor.toArgb())
                        textSize = emojiSize
                        alpha = 1f
                    }
                })
            if (number != null) {
                Text(
                    modifier = Modifier
                        .padding(smallSize),
                    text = number.toString(),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}