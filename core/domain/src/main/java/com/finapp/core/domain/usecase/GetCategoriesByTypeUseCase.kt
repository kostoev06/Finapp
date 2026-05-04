package com.finapp.core.domain.usecase

import com.finapp.core.common.outcome.Outcome
import com.finapp.core.data.api.model.Category
import com.finapp.core.data.api.repository.CategoryRepository
import com.finapp.core.domain.result.LoadResult
import javax.inject.Inject

/**
 * Категории доходов или расходов; на ошибке — из локального кеша.
 * Не зеркалит в локальный источник: для income/expense форм категории и так
 * прогреваются при старте приложения через [GetAllCategoriesUseCase].
 */
class GetCategoriesByTypeUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(isIncome: Boolean): LoadResult<List<Category>> {
        return when (val outcome = categoryRepository.fetchCategoriesByType(isIncome)) {
            is Outcome.Success -> LoadResult(data = outcome.data)
            is Outcome.FailureOutcome -> LoadResult(
                data = categoryRepository.getLocalCategoriesByType(isIncome),
                error = outcome
            )
        }
    }
}
