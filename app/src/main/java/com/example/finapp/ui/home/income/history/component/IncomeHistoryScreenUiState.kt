package com.example.finapp.ui.home.income.history.component

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

@Immutable
data class IncomeHistoryScreenUiState(
    val startDate: LocalDate = LocalDate.now().withDayOfMonth(1),
    val endDate: LocalDate = LocalDate.now(),
    val summary: IncomeHistorySumUiState = IncomeHistorySumUiState(),
    val items: ImmutableList<IncomeHistoryItemUiState> = persistentListOf()
)
