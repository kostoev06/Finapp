package com.example.finapp.ui.home.income.history.component

import com.example.finapp.domain.Transaction
import java.time.format.DateTimeFormatter

/**
 * UI-состояние элемента истории доходов.
 */
data class IncomeHistoryItemUiState(
    val title: String,
    val subtitle: String? = null,
    val amountFormatted: String,
    val timeText: String
)


fun Transaction.toUiState() =
    IncomeHistoryItemUiState(
        title = category.name,
        subtitle = comment,
        amountFormatted = "${amount.stripTrailingZeros().toPlainString()} ₽",
        timeText = createdAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"))
    )
