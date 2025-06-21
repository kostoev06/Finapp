package com.example.finapp.ui.home.income.component

import com.example.finapp.domain.Transaction

data class IncomeItemUiState(
    val title: String,
    val amountFormatted: String
)

fun Transaction.toUiState() =
    IncomeItemUiState(
        title = category.name,
        amountFormatted = "${amount.stripTrailingZeros().toPlainString()} ₽"
    )
