package com.finapp.core.data.impl.model

import com.finapp.core.data.api.model.Category
import com.finapp.core.remote.api.model.CategoryDto

fun CategoryDto.asCategory(): Category =
    Category(
        id = id,
        name = name,
        emoji = emoji,
        isIncome = isIncome
    )