package com.finapp.feature.tags

import com.finapp.core.data.api.model.Category

fun Category.asTagsItemUiState() =
    TagsItemUiState(
        leadingSymbols = emoji,
        title = name
    )