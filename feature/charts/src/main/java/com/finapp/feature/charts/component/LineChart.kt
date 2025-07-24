package com.finapp.feature.charts.component

import android.graphics.Paint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
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
import com.finapp.feature.charts.utils.computeLineChartScaleAndZero
import com.finapp.feature.charts.utils.defaultXAxisBaseline
import com.finapp.feature.charts.utils.drawSimpleTooltip
import com.finapp.feature.charts.utils.drawSymmetricGrid
import com.finapp.feature.charts.utils.drawXAxis3Labels
import com.finapp.feature.charts.utils.rememberTooltipTextPaint
import com.finapp.feature.charts.utils.rememberXAxisPaint
import com.finapp.feature.common.theme.Indigo
import kotlinx.coroutines.delay

@Composable
internal fun LineChart(
    entries: List<ChartEntry>,
    modifier: Modifier = Modifier,
    onEntrySelected: (ChartEntry?) -> Unit
) {
    val progress = remember(entries) { Animatable(0f) }
    LaunchedEffect(entries) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(700, easing = FastOutSlowInEasing)
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
            if (entries.size < 2) return@Canvas

            val stepX = size.width / (entries.size - 1)
            val (scale, zeroY) = computeLineChartScaleAndZero(entries, size.height)

            drawSymmetricGrid(stepX = stepX, zeroY = zeroY)

            val path = Path().apply {
                entries.forEachIndexed { i, e ->
                    val x = i * stepX
                    val y = zeroY - (e.value * scale * progress.value)
                    if (i == 0) moveTo(x, y) else lineTo(x, y)
                }
            }

            drawPath(
                path = path,
                color = Indigo,
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
            )

            entries.forEachIndexed { i, e ->
                val x = i * stepX
                val y = zeroY - (e.value * scale * progress.value)
                drawCircle(
                    color = if (e.color != Color.Unspecified) e.color else Indigo,
                    center = Offset(x, y),
                    radius = 4.dp.toPx() * progress.value
                )
            }

            val firstIdx = 0
            val midIdx = entries.size / 2
            val lastIdx = entries.lastIndex
            val baseline = defaultXAxisBaseline()

            drawXAxis3Labels(
                first = entries[firstIdx].label to (firstIdx * stepX),
                mid = entries[midIdx].label to (midIdx * stepX),
                last = entries[lastIdx].label to (lastIdx * stepX),
                baselineY = baseline,
                paint = xPaint
            )

            selectedIndex?.let { idx ->
                if (idx in entries.indices) {
                    val e = entries[idx]
                    val x = idx * stepX
                    val y = zeroY - (e.value * scale * progress.value)

                    drawSimpleTooltip(
                        text = e.label,
                        anchor = Offset(x, y),
                        textPaint = tooltipPaint,
                        alpha = tooltipAlpha.value
                    )
                }
            }
        }
    }
}