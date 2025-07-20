package com.finapp.feature.tags

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finapp.core.common.outcome.handleOutcome
import com.finapp.core.data.api.model.Category
import com.finapp.core.data.api.repository.CategoryRepository
import com.finapp.feature.common.di.ViewModelAssistedFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
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
class TagsViewModel @AssistedInject constructor(
    private val categoryRepository: CategoryRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(TagsScreenUiState())
    val uiState: StateFlow<TagsScreenUiState> = _uiState.asStateFlow()

    private var allItems: ImmutableList<TagsItemUiState> = persistentListOf()

    init {
        viewModelScope.launch {
            categoryRepository.fetchCategories().handleOutcome {
                onSuccess {
                    updateUiState(data)
                    categoryRepository.insertAllLocalCategories(data)
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

    private fun updateUiState(data: List<Category>) {
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

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<TagsViewModel>

}
