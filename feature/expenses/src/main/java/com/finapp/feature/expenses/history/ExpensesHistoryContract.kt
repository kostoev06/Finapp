package com.finapp.feature.expenses.history

import androidx.compose.runtime.Immutable
import com.finapp.core.data.api.model.CurrencyCode
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

/**
 * UI-состояние элемента истории расходов.
 */
data class ExpensesHistoryItemUiState(
    val id: Long?,
    val leadingSymbols: String? = null,
    val title: String,
    val subtitle: String? = null,
    val amount: String,
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
    val items: ImmutableList<ExpensesHistoryItemUiState> = persistentListOf(),
    val currency: CurrencyCode = CurrencyCode.RUB
)

/**
 * UI-состояние элемента суммы расходов из истории.
 */
data class ExpensesHistorySumUiState(
    val totalAmount: String = "0"
)