package com.finapp.feature.income.history

import com.finapp.core.data.api.model.Transaction
import com.finapp.feature.common.utils.toFormattedString
import java.time.format.DateTimeFormatter

fun Transaction.asIncomeHistoryItemUiState() =
    IncomeHistoryItemUiState(
        id = id,
        title = category.name,
        subtitle = comment?.ifBlank { null },
        amount = amount.toFormattedString(),
        timeText = createdAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"))
    )