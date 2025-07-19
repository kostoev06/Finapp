package com.finapp.feature.expenses.homepage

import com.finapp.core.data.api.model.CurrencyCode
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * UI-состояние элемента расхода.
 */
data class ExpensesItemUiState(
    val id: Long,
    val leadingSymbols: String? = null,
    val title: String,
    val subtitle: String? = null,
    val amount: String
)

/**
 * UI-состояние экрана расходов.
 */
data class ExpensesScreenUiState(
    val summary: ExpensesSumUiState = ExpensesSumUiState(),
    val items: ImmutableList<ExpensesItemUiState> = persistentListOf(),
    val currency: CurrencyCode = CurrencyCode.RUB
)

/**
 * UI-состояние элемента суммы расходов.
 */
data class ExpensesSumUiState(
    val totalAmount: String = "0"
)