package com.finapp.feature.tags

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * UI-состояние элемента статей.
 */
data class TagsItemUiState(
    val leadingSymbols: String,
    val title: String
)

/**
 * UI-состояние строки поиска.
 */
data class SearchUiState(
    val query: String = ""
)

/**
 * UI-состояние экрана статей.
 */
data class TagsScreenUiState(
    val search: SearchUiState = SearchUiState(),
    val items: ImmutableList<TagsItemUiState> = persistentListOf()
)