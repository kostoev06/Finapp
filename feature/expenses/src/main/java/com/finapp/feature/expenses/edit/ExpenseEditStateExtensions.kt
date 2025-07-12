package com.finapp.feature.expenses.edit

import com.finapp.core.data.api.model.Category

fun Category.asCategoryUiState() =
    CategoryUiState(
        id = id,
        name = name
    )