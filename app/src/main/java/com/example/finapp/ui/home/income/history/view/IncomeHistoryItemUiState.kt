package com.example.finapp.ui.home.income.history.view

import com.example.finapp.domain.Transaction
import java.time.format.DateTimeFormatter


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
