package com.example.finapp.ui.feature.tags

import com.example.finapp.domain.Category

fun Category.asTagsItemUiState() =
    TagsItemUiState(
        leadingSymbols = emoji,
        title = name
    )