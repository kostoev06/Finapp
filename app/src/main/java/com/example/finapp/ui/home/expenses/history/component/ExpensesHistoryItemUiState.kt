package com.example.finapp.ui.home.expenses.history.component

import com.example.finapp.domain.Transaction
import java.time.format.DateTimeFormatter

/**
 * UI-состояние элемента истории расходов.
 */
data class ExpensesHistoryItemUiState(
    val leadingSymbols: String? = null,
    val title: String,
    val subtitle: String? = null,
    val amountFormatted: String,
    val timeText: String
)

fun Transaction.toUiState() =
    ExpensesHistoryItemUiState(
        leadingSymbols = category.emoji,
        title = category.name,
        subtitle = comment,
        amountFormatted = "${amount.stripTrailingZeros().toPlainString()} ₽",
        timeText = createdAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"))
    )
