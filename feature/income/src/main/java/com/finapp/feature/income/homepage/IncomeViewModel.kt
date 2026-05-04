package com.finapp.feature.income.homepage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finapp.core.data.api.model.CurrencyCode
import com.finapp.core.data.api.model.Transaction
import com.finapp.core.data.api.repository.CurrencyRepository
import com.finapp.core.domain.usecase.GetTransactionsByPeriodUseCase
import com.finapp.feature.common.di.ViewModelAssistedFactory
import com.finapp.feature.common.utils.toFormattedString
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * ViewModel для экрана доходов.
 */
class IncomeViewModel @AssistedInject constructor(
    private val getTransactionsByPeriod: GetTransactionsByPeriodUseCase,
    currencyRepository: CurrencyRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(IncomeScreenUiState())
    val uiState: StateFlow<IncomeScreenUiState> = _uiState.asStateFlow()

    init {
        currencyRepository.currency
            .onEach { code -> _uiState.update { it.copy(currency = CurrencyCode.from(code)) } }
            .launchIn(viewModelScope)
        loadIncome()
    }

    fun loadIncome() {
        viewModelScope.launch {
            val today = LocalDate.now()
            val result = getTransactionsByPeriod(start = today, end = today)
            updateUiState(result.data)
        }
    }

    private fun updateUiState(data: List<Transaction>) {
        val income = data.filter { it.category.isIncome }
        val total = income.sumOf { it.amount }.toFormattedString()
        _uiState.update {
            it.copy(
                summary = IncomeSumUiState(totalAmount = total),
                items = income
                    .map { transaction -> transaction.asIncomeItemUiState() }
                    .toImmutableList()
            )
        }
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<IncomeViewModel>
}
