package com.example.finapp.ui.home.expenses.history.component

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

/**
 * UI-состояние экрана истории расходов.
 */
@Immutable
data class ExpensesHistoryScreenUiState(
    val startDate: LocalDate = LocalDate.now().withDayOfMonth(1),
    val endDate: LocalDate = LocalDate.now(),
    val summary: ExpensesHistorySumUiState = ExpensesHistorySumUiState(),
    val items: ImmutableList<ExpensesHistoryItemUiState> = persistentListOf()
)
