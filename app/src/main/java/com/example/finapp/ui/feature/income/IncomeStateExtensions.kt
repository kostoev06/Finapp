package com.example.finapp.ui.feature.income

import com.example.finapp.domain.Transaction
import com.example.finapp.ui.utils.toFormattedString

fun Transaction.asIncomeItemUiState() =
    IncomeItemUiState(
        title = category.name,
        amount = amount.toFormattedString()
    )