package com.example.finapp.ui.home.tags.component

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class TagsScreenUiState(
    val search: SearchUiState = SearchUiState(),
    val items: ImmutableList<TagsItemUiState> = persistentListOf()
)
