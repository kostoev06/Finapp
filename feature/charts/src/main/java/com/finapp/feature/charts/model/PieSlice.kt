package com.finapp.feature.charts.model

import androidx.compose.ui.graphics.Color

data class PieSlice(
    val label: String,
    val percent: Float,
    val color: Color
)
