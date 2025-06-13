package com.example.finapp.home.tags.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.finapp.home.tags.view.SearchUiState
import com.example.finapp.home.tags.view.TagsItemUiState
import com.example.finapp.home.tags.view.TagsScreenUiState

class TagsViewModel : ViewModel() {
    private val allItems = listOf(
        TagsItemUiState("🏡", "Аренда квартиры"),
        TagsItemUiState("👗", "Одежда"),
        TagsItemUiState("🐶", "На собачку"),
        TagsItemUiState("🐶", "На собачку"),
        TagsItemUiState("РК", "Ремонт квартиры"),
        TagsItemUiState("🍭", "Продукты"),
        TagsItemUiState("🏋️", "Спортзал"),
        TagsItemUiState("💊", "Медицина")
    )

    private val _uiState = mutableStateOf(
        TagsScreenUiState(
            search = SearchUiState(),
            items  = allItems
        )
    )
    val uiState: State<TagsScreenUiState> = _uiState

    fun onSearchQueryChange(newQuery: String) {
        val filtered = if (newQuery.isBlank()) allItems
        else allItems.filter { it.title.contains(newQuery, ignoreCase = true) }

        _uiState.value = _uiState.value.copy(
            search = SearchUiState(query = newQuery),
            items  = filtered
        )
    }
}
