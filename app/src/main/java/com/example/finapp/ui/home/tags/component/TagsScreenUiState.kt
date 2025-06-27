package com.example.finapp.ui.home.tags.component

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * UI-состояние экрана статей.
 */
data class TagsScreenUiState(
    val search: SearchUiState = SearchUiState(),
    val items: ImmutableList<TagsItemUiState> = persistentListOf()
)
