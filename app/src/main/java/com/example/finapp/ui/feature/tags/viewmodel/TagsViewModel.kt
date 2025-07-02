package com.example.finapp.ui.feature.tags.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finapp.data.common.handleOutcome
import com.example.finapp.data.repository.CategoriesRepository
import com.example.finapp.ui.feature.tags.SearchUiState
import com.example.finapp.ui.feature.tags.TagsItemUiState
import com.example.finapp.ui.feature.tags.TagsScreenUiState
import com.example.finapp.ui.feature.tags.asTagsItemUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel для экрана статей.
 */
class TagsViewModel(
    private val categoriesRepository: CategoriesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TagsScreenUiState())
    val uiState: StateFlow<TagsScreenUiState> = _uiState.asStateFlow()

    private var allItems: ImmutableList<TagsItemUiState> = persistentListOf()

    init {
        viewModelScope.launch {
            categoriesRepository.getAllCategories().handleOutcome {
                onSuccess {
                    val uiList = data
                        .map { dto -> dto.asTagsItemUiState() }
                        .toPersistentList()

                    allItems = uiList

                    _uiState.update {
                        it.copy(
                            items = uiList
                        )
                    }
                }
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        viewModelScope.launch {
            val filtered = if (newQuery.isBlank()) {
                allItems
            } else {
                allItems
                    .filter { it.title.contains(newQuery, ignoreCase = true) }
                    .toPersistentList()
            }

            _uiState.update { current ->
                current.copy(
                    search = SearchUiState(query = newQuery),
                    items = filtered
                )
            }
        }
    }

}
