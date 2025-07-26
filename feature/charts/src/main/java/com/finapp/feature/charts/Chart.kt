package com.finapp.feature.charts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.finapp.feature.charts.component.BarChart
import com.finapp.feature.charts.component.LineChart
import com.finapp.feature.charts.model.ChartEntry
import com.finapp.feature.charts.model.ChartType

@Composable
fun Chart(
    entries: List<ChartEntry>,
    type: ChartType,
    modifier: Modifier = Modifier,
    onEntrySelected: (ChartEntry?) -> Unit = {}
) = when (type) {
    ChartType.LINE -> LineChart(entries, modifier, onEntrySelected)
    ChartType.BAR -> BarChart(entries, modifier, onEntrySelected)
}