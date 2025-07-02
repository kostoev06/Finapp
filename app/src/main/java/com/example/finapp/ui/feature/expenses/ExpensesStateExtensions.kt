package com.example.finapp.ui.feature.expenses

import com.example.finapp.domain.Transaction

fun Transaction.asExpensesItemUiState() =
    ExpensesItemUiState(
        leadingSymbols = category.emoji,
        title = category.name,
        subtitle = comment,
        amountFormatted = "${amount.stripTrailingZeros().toPlainString()} ₽"
    )