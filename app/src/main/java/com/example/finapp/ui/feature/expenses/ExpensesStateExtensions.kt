package com.example.finapp.ui.feature.expenses

import com.example.finapp.domain.Transaction
import com.example.finapp.ui.utils.toFormattedString

fun Transaction.asExpensesItemUiState() =
    ExpensesItemUiState(
        leadingSymbols = category.emoji,
        title = category.name,
        subtitle = comment,
        amount = amount.toFormattedString()
    )