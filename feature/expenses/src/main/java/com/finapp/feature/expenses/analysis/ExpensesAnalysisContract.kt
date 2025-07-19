package com.finapp.feature.expenses.analysis

import androidx.compose.runtime.Immutable
import com.finapp.core.data.api.model.CurrencyCode
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

/**
 * UI-состояние элемента истории расходов.
 */
data class ExpensesAnalysisItemUiState(
    val categoryId: Long,
    val leadingSymbols: String? = null,
    val title: String,
    val amount: String,
    val percent: String
)

/**
 * UI-состояние экрана истории расходов.
 */
@Immutable
data class ExpensesAnalysisScreenUiState(
    val isLoading: Boolean = true,
    val startDate: LocalDate = LocalDate.now().withDayOfMonth(1),
    val endDate: LocalDate = LocalDate.now(),
    val summary: ExpensesAnalysisSumUiState = ExpensesAnalysisSumUiState(),
    val items: ImmutableList<ExpensesAnalysisItemUiState> = persistentListOf(),
    val currency: CurrencyCode = CurrencyCode.RUB
)

/**
 * UI-состояние элемента суммы расходов из истории.
 */
data class ExpensesAnalysisSumUiState(
    val totalAmount: String = "0"
)