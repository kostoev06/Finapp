package com.finapp.feature.charts.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finapp.feature.charts.R
import com.finapp.feature.charts.model.PieSlice
import com.finapp.feature.common.theme.PieGrey
import kotlin.math.min

@Composable
fun DonutChart(
    slices: List<PieSlice>,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 32.dp,
    labelTextSizeSp: Float = 7f,
    maxLegendItems: Int = 6,
    radiusFraction: Float = 0.85f,
    legendItemSpacing: Dp = 2.dp,
    legendDotSize: Dp = 6.dp
) {
    val anim = remember(slices) { Animatable(0f) }
    LaunchedEffect(slices) {
        anim.snapTo(0f)
        anim.animateTo(1f, tween(800, easing = FastOutSlowInEasing))
    }

    val sorted = slices.sortedByDescending { it.percent }
    val shown = sorted.take(maxLegendItems)
    val others = sorted.drop(maxLegendItems).sumOf { it.percent.toDouble() }.toFloat()
    val legendItems =
        if (others > 0f) shown + PieSlice(
            stringResource(R.string.other),
            others,
            PieGrey
        ) else shown

    Box(modifier = modifier, contentAlignment = Alignment.Center) {

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Butt)
            val diameter = min(size.width, size.height) * radiusFraction
            val center = Offset(size.width / 2f, size.height / 2f)
            val outerR = diameter / 2f
            val rect = Rect(
                Offset(center.x - outerR, center.y - outerR),
                size = Size(diameter, diameter)
            )

            var startAngle = -90f
            slices.forEach { slice ->
                val sweep = 360f * (slice.percent / 100f) * anim.value
                drawArc(
                    color = slice.color,
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = stroke,
                    topLeft = rect.topLeft,
                    size = rect.size
                )
                startAngle += sweep
            }
        }

        LegendInsideDonut(
            legendItems = legendItems,
            textSizeSp = labelTextSizeSp,
            spacing = legendItemSpacing,
            dotSize = legendDotSize
        )
    }
}

@Composable
private fun LegendInsideDonut(
    legendItems: List<PieSlice>,
    textSizeSp: Float,
    spacing: Dp,
    dotSize: Dp
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(spacing),
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        legendItems.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
            ) {
                Canvas(
                    modifier = Modifier.size(dotSize)
                ) {
                    drawCircle(color = item.color)
                }
                Spacer(Modifier.width(4.dp))
                androidx.compose.material3.Text(
                    text = "${item.percent.toInt()}% ${item.label}",
                    fontSize = textSizeSp.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = textSizeSp.sp
                )
            }
        }
    }
}