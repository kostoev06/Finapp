package com.finapp.feature.income.homepage

import com.finapp.core.data.api.model.Transaction
import com.finapp.feature.common.utils.toFormattedString


fun Transaction.asIncomeItemUiState() =
    IncomeItemUiState(
        id = id,
        title = category.name,
        amount = amount.toFormattedString()
    )