package com.example.finapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finapp.R
import com.example.finapp.ui.theme.GreenPrimaryLight
import java.text.BreakIterator
import java.util.Locale


@Composable
fun FinappListItem(
    title: String,
    modifier: Modifier = Modifier,
    leadingSymbols: String? = null,
    subtitle: String? = null,
    trailingContent: (@Composable FinanceListItemTrailingScope.() -> Unit)? = null,
    trailingIcon: @Composable (RowScope.() -> Unit)? = null,
    height: Int = 70,
    green: Boolean = false,
    clickable: Boolean = false,
    onClick: () -> Unit = {}
) {
    val titleScope = remember { FinanceListItemTrailingScope() }
    val isEmoji = remember(leadingSymbols) { leadingSymbols?.let { isSingleEmoji(it) } == true }

    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
            .let { base -> if (clickable) base.clickable(onClick = onClick) else base },

        leadingContent = if (leadingSymbols != null) {
            {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(
                            color = if (green) Color.White else GreenPrimaryLight,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = leadingSymbols,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = if (isEmoji) 16.sp else 10.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.W500
                    )
                }
            }
        } else null,

        headlineContent = { Text(title) },
        supportingContent = if (subtitle != null) { { Text(subtitle) } } else null,

        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                with(titleScope) {
                    trailingContent?.invoke(this)
                }
                if (trailingContent != null && trailingIcon != null) {
                    Spacer(Modifier.width(16.dp))
                }
                trailingIcon?.invoke(this)
            }
        },

        colors = if (green) {
            ListItemDefaults.colors(containerColor = GreenPrimaryLight)
        } else {
            ListItemDefaults.colors()
        }

    )
    HorizontalDivider()
}

@Stable
class FinanceListItemTrailingScope {
    @Composable
    fun Text(
        text: String,
        modifier: Modifier = Modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = modifier
        )
    }

    @Composable
    fun Switch(
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
        modifier: Modifier = Modifier
    ) {
        androidx.compose.material3.Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = modifier
        )
    }
}


private fun isEmojiCodePoint(cp: Int): Boolean {
    if (cp == 0x200D || (cp in 0xFE00..0xFE0F)) return true
    val block = Character.UnicodeBlock.of(cp) ?: return false
    return when (block) {
        Character.UnicodeBlock.EMOTICONS,
        Character.UnicodeBlock.MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS,
        Character.UnicodeBlock.TRANSPORT_AND_MAP_SYMBOLS,
        Character.UnicodeBlock.MISCELLANEOUS_SYMBOLS -> true
        else -> false
    }
}

private fun splitGraphemes(text: String): List<String> {
    val it = BreakIterator.getCharacterInstance(Locale.getDefault())
    it.setText(text)
    val graphemes = mutableListOf<String>()
    var start = it.first()
    var end = it.next()
    while (end != BreakIterator.DONE) {
        graphemes += text.substring(start, end)
        start = end
        end = it.next()
    }
    return graphemes
}

private fun isSingleEmoji(text: String): Boolean {
    val clusters = splitGraphemes(text)
    if (clusters.size != 1) return false
    val cluster = clusters[0]
    var i = 0
    var foundBaseEmoji = false
    while (i < cluster.length) {
        val cp = cluster.codePointAt(i)
        val charCount = Character.charCount(cp)
        if (cp == 0x200D || (cp in 0xFE00..0xFE0F)) {
            i += charCount
            continue
        }
        if (isEmojiCodePoint(cp)) {
            foundBaseEmoji = true
        } else {
            return false
        }
        i += charCount
    }
    return foundBaseEmoji
}


@Preview(showBackground = true, widthDp = 412)
@Composable
fun FinanceListItem_Preview() {
    androidx.compose.material3.Surface {
        FinappListItem(
            leadingSymbols = "🏡",
            title = "Аренда квартиры",
//            subtitle = "ЖК «Лазурный»",
            trailingContent = { Text("100 000 ₽") },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right_1),
                    contentDescription = null
                )
            },
            onClick = { },
            green = false
        )
    }
}