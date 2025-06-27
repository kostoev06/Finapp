package com.example.finapp.ui.home.expenses.component

import com.example.finapp.domain.Transaction

/**
 * UI-состояние элемента расхода.
 */
data class ExpensesItemUiState(
    val leadingSymbols: String? = null,
    val title: String,
    val subtitle: String? = null,
    val amountFormatted: String
)

fun Transaction.toUiState() =
    ExpensesItemUiState(
        leadingSymbols = category.emoji,
        title = category.name,
        subtitle = comment,
        amountFormatted = "${amount.stripTrailingZeros().toPlainString()} ₽"
    )
