package com.finapp.core.domain.usecase

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.data.api.model.Category
import com.finapp.core.data.api.repository.CategoryRepository
import com.finapp.core.domain.result.LoadResult
import javax.inject.Inject

/**
 * Все категории; на ошибке — из локального кеша. Успешный ответ зеркалит в локальный источник.
 */
class GetAllCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): LoadResult<List<Category>> {
        return when (val outcome = categoryRepository.fetchCategories()) {
            is Outcome.Success -> {
                categoryRepository.insertAllLocalCategories(outcome.data)
                LoadResult(data = outcome.data)
            }
            is Outcome.FailureOutcome -> LoadResult(
                data = categoryRepository.getAllLocalCategories(),
                error = outcome
            )
        }
    }
}
