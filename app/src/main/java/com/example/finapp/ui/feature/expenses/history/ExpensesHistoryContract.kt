package com.example.finapp.ui.feature.expenses.history

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

/**
 * UI-состояние элемента истории расходов.
 */
data class ExpensesHistoryItemUiState(
    val leadingSymbols: String? = null,
    val title: String,
    val subtitle: String? = null,
    val amountFormatted: String,
    val timeText: String
)

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

/**
 * UI-состояние элемента суммы расходов из истории.
 */
data class ExpensesHistorySumUiState(
    val totalFormatted: String = "0 ₽"
)