package com.example.finapp.ui.feature.expenses

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * UI-состояние элемента расхода.
 */
data class ExpensesItemUiState(
    val leadingSymbols: String? = null,
    val title: String,
    val subtitle: String? = null,
    val amountFormatted: String
)

/**
 * UI-состояние экрана расходов.
 */
data class ExpensesScreenUiState(
    val summary: ExpensesSumUiState = ExpensesSumUiState(),
    val items: ImmutableList<ExpensesItemUiState> = persistentListOf()
)

/**
 * UI-состояние элемента суммы расходов.
 */
data class ExpensesSumUiState(
    val totalFormatted: String = "0 ₽"
)