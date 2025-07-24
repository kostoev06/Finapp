package com.finapp.feature.charts.component

import android.graphics.Paint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.pointerInput
import com.finapp.feature.charts.model.ChartEntry
import com.finapp.feature.charts.utils.nearestEntry
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.finapp.feature.charts.utils.defaultXAxisBaseline
import com.finapp.feature.charts.utils.drawSimpleTooltip
import com.finapp.feature.charts.utils.drawXAxis3Labels
import com.finapp.feature.charts.utils.rememberTooltipTextPaint
import com.finapp.feature.charts.utils.rememberXAxisPaint
import com.finapp.feature.common.theme.GreenPrimary
import com.finapp.feature.common.theme.Orange
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.max

@Composable
internal fun BarChart(
    entries: List<ChartEntry>,
    modifier: Modifier = Modifier,
    onEntrySelected: (ChartEntry?) -> Unit
) {
    val progress = remember(entries) { Animatable(0f) }
    LaunchedEffect(entries) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(600, easing = FastOutSlowInEasing)
        )
    }

    val xPaint: Paint = rememberXAxisPaint()
    val tooltipPaint: Paint = rememberTooltipTextPaint()

    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val tooltipAlpha = remember { Animatable(0f) }

    LaunchedEffect(selectedIndex) {
        if (selectedIndex != null) {
            tooltipAlpha.snapTo(0f)
            tooltipAlpha.animateTo(1f, tween(150, easing = FastOutSlowInEasing))
            delay(1500)
            tooltipAlpha.animateTo(0f, tween(200, easing = FastOutSlowInEasing))
            selectedIndex = null
        }
    }

    Box(
        modifier = modifier.pointerInput(entries) {
            detectTapGestures { offset ->
                val hit = nearestEntry(offset, entries, size)
                selectedIndex = hit?.let { entries.indexOf(it) }
                onEntrySelected(hit)
            }
        }
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            if (entries.isEmpty()) return@Canvas

            val stepX = size.width / entries.size
            val barWidth = stepX * 0.6f
            val maxVal = entries.maxOfOrNull { it.value } ?: 0f
            val minVal = entries.minOfOrNull { it.value } ?: 0f
            val maxAbs = max(maxVal, abs(minVal)).takeIf { it != 0f } ?: 1f
            val scale = size.height / maxAbs
            val minBarPx = 2.dp.toPx()
            val radius = 6.dp.toPx()

            entries.forEachIndexed { i, e ->
                val fullHeight = abs(e.value) * scale
                val animatedH =
                    max(fullHeight * progress.value, if (e.value == 0f) minBarPx else 0f)

                val left = i * stepX + (stepX - barWidth) / 2f
                val top = size.height - animatedH

                val color = when {
                    e.color != Color.Unspecified -> e.color
                    e.value > 0f -> GreenPrimary
                    e.value < 0f -> Orange
                    else -> Color.Gray
                }

                drawRoundRect(
                    color = color,
                    topLeft = Offset(left, top),
                    size = Size(barWidth, animatedH),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(radius, radius),
                    style = Fill
                )
            }

            val firstIdx = 0
            val midIdx = entries.size / 2
            val lastIdx = entries.lastIndex
            val baseline = defaultXAxisBaseline()

            drawXAxis3Labels(
                first = entries[firstIdx].label to (firstIdx * stepX + stepX / 2f),
                mid = entries[midIdx].label to (midIdx * stepX + stepX / 2f),
                last = entries[lastIdx].label to (lastIdx * stepX + stepX / 2f),
                baselineY = baseline,
                paint = xPaint
            )

            selectedIndex?.let { idx ->
                if (idx in entries.indices) {
                    val e = entries[idx]
                    val xCenter = idx * stepX + stepX / 2f
                    val fullH = abs(e.value) * scale
                    val animatedH =
                        max(fullH * progress.value, if (e.value == 0f) minBarPx else 0f)
                    val top = size.height - animatedH

                    drawSimpleTooltip(
                        text = e.label,
                        anchor = Offset(xCenter, top),
                        textPaint = tooltipPaint,
                        alpha = tooltipAlpha.value
                    )
                }
            }
        }
    }
}