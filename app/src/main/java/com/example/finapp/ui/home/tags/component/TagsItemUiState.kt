package com.example.finapp.ui.home.tags.component

import com.example.finapp.domain.Category

/**
 * UI-состояние элемента статей.
 */
data class TagsItemUiState(
    val leadingSymbols: String,
    val title: String
)

fun Category.toUiState() =
    TagsItemUiState(
        leadingSymbols = emoji,
        title = name
    )
