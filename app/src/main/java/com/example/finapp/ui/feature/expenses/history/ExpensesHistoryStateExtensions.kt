package com.example.finapp.ui.feature.expenses.history

import com.example.finapp.domain.Transaction
import com.example.finapp.ui.utils.toFormattedString
import java.time.format.DateTimeFormatter

fun Transaction.asExpensesHistoryItemUiState() =
    ExpensesHistoryItemUiState(
        leadingSymbols = category.emoji,
        title = category.name,
        subtitle = comment,
        amount = amount.toFormattedString(),
        timeText = createdAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"))
    )