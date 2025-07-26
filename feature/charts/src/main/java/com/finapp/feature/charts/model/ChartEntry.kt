package com.finapp.feature.charts.model

import androidx.compose.ui.graphics.Color

data class ChartEntry(
    val label: String,
    val value: Float,
    val color: Color = Color.Unspecified
)
