package com.finapp.feature.charts.utils

import android.graphics.Paint
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalDensity


@Composable
internal fun rememberXAxisPaint(
    sizeSp: Float = 12f,
    color: Color = MaterialTheme.colorScheme.onSurface
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

internal fun DrawScope.drawXAxis3Labels(
    first: Pair<String, Float>,
    mid: Pair<String, Float>,
    last: Pair<String, Float>,
    baselineY: Float,
    paint: Paint
) {
    drawIntoCanvas { canvas ->
        canvas.nativeCanvas.drawText(first.first, first.second, baselineY, paint)
        canvas.nativeCanvas.drawText(mid.first, mid.second, baselineY, paint)
        canvas.nativeCanvas.drawText(last.first, last.second, baselineY, paint)
    }
}

internal fun DrawScope.defaultXAxisBaseline(): Float =
    size.height + 16.dp.toPx()
