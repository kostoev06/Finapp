package com.example.finapp.ui.feature.income

import com.example.finapp.domain.Transaction

fun Transaction.asIncomeItemUiState() =
    IncomeItemUiState(
        title = category.name,
        amountFormatted = "${amount.stripTrailingZeros().toPlainString()} ₽"
    )