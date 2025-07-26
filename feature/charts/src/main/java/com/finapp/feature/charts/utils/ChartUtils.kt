package com.finapp.feature.charts.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.finapp.feature.charts.model.ChartEntry
import kotlin.math.abs
import kotlin.math.max


internal val GRID_STROKE = 1.dp
internal val ZERO_LINE_STROKE = 2.dp

internal fun IntSize.toSize(): Size = Size(width.toFloat(), height.toFloat())

internal fun DrawScope.drawGrid(
    stepX: Float,
    horizontalSteps: Int = 4,
    color: Color = Color.LightGray,
    includeBorders: Boolean = true
) {
    val vStroke = GRID_STROKE.toPx()

    var x = 0f
    while (x <= size.width) {
        drawLine(
            color = color,
            start = Offset(x, 0f),
            end = Offset(x, size.height),
            strokeWidth = vStroke
        )
        x += stepX
    }
    if (includeBorders) {
        drawLine(color, Offset(size.width, 0f), Offset(size.width, size.height), vStroke)
        drawLine(color, Offset(0f, 0f), Offset(size.width, 0f), vStroke)
        drawLine(color, Offset(0f, size.height), Offset(size.width, size.height), vStroke)
    }

    val stepY = size.height / horizontalSteps
    var y = 0f
    while (y <= size.height) {
        drawLine(
            color = color,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = vStroke
        )
        y += stepY
    }
}

internal fun DrawScope.drawSymmetricGrid(
    stepX: Float,
    zeroY: Float,
    stepsEach: Int = 3,
    color: Color = Color.LightGray,
    zeroLineColor: Color = Color.Gray,
    includeBorders: Boolean = true
) {
    val stroke = GRID_STROKE.toPx()

    var x = 0f
    while (x <= size.width) {
        drawLine(color, Offset(x, 0f), Offset(x, size.height), stroke)
        x += stepX
    }
    if (includeBorders) {
        drawLine(color, Offset(size.width, 0f), Offset(size.width, size.height), stroke)
        drawLine(color, Offset(0f, 0f), Offset(size.width, 0f), stroke)
        drawLine(color, Offset(0f, size.height), Offset(size.width, size.height), stroke)
    }

    drawLine(
        color = zeroLineColor,
        start = Offset(0f, zeroY),
        end = Offset(size.width, zeroY),
        strokeWidth = ZERO_LINE_STROKE.toPx()
    )

    val upStep = zeroY / stepsEach.coerceAtLeast(1)
    val downStep = (size.height - zeroY) / stepsEach.coerceAtLeast(1)

    var yUp = zeroY - upStep
    repeat(stepsEach) {
        drawLine(color, Offset(0f, yUp), Offset(size.width, yUp), stroke)
        yUp -= upStep
    }

    var yDown = zeroY + downStep
    repeat(stepsEach) {
        drawLine(color, Offset(0f, yDown), Offset(size.width, yDown), stroke)
        yDown += downStep
    }
}

internal fun nearestEntry(
    tap: Offset,
    entries: List<ChartEntry>,
    canvas: Size
): ChartEntry? {
    if (entries.isEmpty()) return null
    val stepX = if (entries.size > 1) canvas.width / (entries.size - 1) else canvas.width
    var min = Float.MAX_VALUE
    var hit: ChartEntry? = null

    entries.forEachIndexed { i, e ->
        val x = if (entries.size > 1) i * stepX else canvas.width / 2f
        val d = abs(tap.x - x)
        if (d < min) {
            min = d
            hit = e
        }
    }
    return if (entries.size > 1 && min > stepX / 2f) null else hit
}

internal fun nearestEntry(
    tap: Offset,
    entries: List<ChartEntry>,
    canvas: IntSize
): ChartEntry? = nearestEntry(tap, entries, canvas.toSize())

internal fun computeLineChartScaleAndZero(
    entries: List<ChartEntry>,
    canvasHeight: Float
): Pair<Float, Float> {
    val minVal = entries.minOfOrNull { it.value } ?: 0f
    val maxVal = entries.maxOfOrNull { it.value } ?: 0f
    val maxAbs = max(abs(minVal), abs(maxVal)).takeIf { it != 0f } ?: 1f
    val half = canvasHeight / 2f
    val scale = half / maxAbs
    val zeroY = half
    return scale to zeroY
}