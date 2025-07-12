package com.finapp.core.data.impl.model

import com.finapp.core.data.api.model.StatItem
import com.finapp.core.remote.api.model.StatItemDto

fun StatItemDto.asTransaction(): StatItem =
    StatItem(
        categoryId = categoryId,
        categoryName = categoryName,
        emoji = emoji,
        amount = amount.toBigDecimal()
    )