package com.finapp.feature.charts.utils

import android.graphics.Paint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.nativeCanvas

internal fun Paint.setAlphaFraction(fraction: Float) {
    alpha = (255 * fraction.coerceIn(0f, 1f)).toInt()
}

@Composable
internal fun rememberTooltipTextPaint(
    sizeSp: Float = 12f,
    color: Color = Color.White
): Paint {
    val density = LocalDensity.current
    return remember(sizeSp, color) {
        Paint().apply {
            isAntiAlias = true
            textSize = with(density) { sizeSp.sp.toPx() }
            this.color = color.toArgb()
            textAlign = Paint.Align.CENTER
        }
    }
}

internal fun DrawScope.drawSimpleTooltip(
    text: String,
    anchor: Offset,
    textPaint: Paint,
    alpha: Float,
    bgColor: Color = Color.Black.copy(alpha = 0.8f),
    paddingPx: Float = 6.dp.toPx(),
    cornerPx: Float = 6.dp.toPx(),
    gapPx: Float = 4.dp.toPx()
) {
    if (alpha <= 0f) return

    val fm = textPaint.fontMetrics
    val textH = fm.descent - fm.ascent
    val textW = textPaint.measureText(text)

    val rectW = textW + paddingPx * 2
    val rectH = textH + paddingPx * 2

    var left = anchor.x - rectW / 2f
    var top = anchor.y - rectH - gapPx

    left = left.coerceIn(0f, size.width - rectW)
    top = top.coerceIn(0f, size.height - rectH)

    val right = left + rectW

    drawRoundRect(
        color = bgColor.copy(alpha = bgColor.alpha * alpha),
        topLeft = Offset(left, top),
        size = Size(rectW, rectH),
        cornerRadius = CornerRadius(cornerPx, cornerPx)
    )

    val baseline = top + paddingPx - fm.ascent
    val oldAlpha = textPaint.alpha
    textPaint.setAlphaFraction(alpha)

    drawIntoCanvas { c ->
        c.nativeCanvas.drawText(text, (left + right) / 2f, baseline, textPaint)
    }

    textPaint.alpha = oldAlpha
}