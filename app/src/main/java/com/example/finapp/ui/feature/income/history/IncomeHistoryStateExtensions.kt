package com.example.finapp.ui.feature.income.history

import com.example.finapp.domain.Transaction
import java.time.format.DateTimeFormatter

fun Transaction.asIncomeHistoryItemUiState() =
    IncomeHistoryItemUiState(
        title = category.name,
        subtitle = comment,
        amountFormatted = "${amount.stripTrailingZeros().toPlainString()} ₽",
        timeText = createdAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"))
    )