package com.example.finapp.ui.home.tags.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finapp.data.repository.CategoriesRepository
import com.example.finapp.data.repository.impl.CategoriesRepositoryImpl

/**
 * Фабрика для создания TagsViewModel с передачей зависимостей.
 */
class TagsViewModelFactory(
    private val categoriesRepository: CategoriesRepository = CategoriesRepositoryImpl()
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TagsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TagsViewModel(categoriesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
