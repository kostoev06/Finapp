package com.finapp.feature.expenses

import com.finapp.core.data.api.model.Transaction
import com.finapp.feature.common.utils.toFormattedString


fun Transaction.asExpensesItemUiState() =
    ExpensesItemUiState(
        id = id,
        leadingSymbols = category.emoji,
        title = category.name,
        subtitle = comment?.ifBlank { null },
        amount = amount.toFormattedString()
    )