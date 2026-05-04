package com.finapp.feature.expenses.history

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
 * ViewModel для экрана истории расходов.
 */
class ExpensesHistoryViewModel @AssistedInject constructor(
    private val getTransactionsByPeriod: GetTransactionsByPeriodUseCase,
    currencyRepository: CurrencyRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpensesHistoryScreenUiState())
    val uiState: StateFlow<ExpensesHistoryScreenUiState> = _uiState.asStateFlow()

    init {
        currencyRepository.currency
            .onEach { code -> _uiState.update { it.copy(currency = CurrencyCode.from(code)) } }
            .launchIn(viewModelScope)
        loadExpensesHistory()
    }

    fun onChooseStartDate(startDate: LocalDate) {
        _uiState.update { it.copy(startDate = startDate) }
        reload()
    }

    fun onChooseEndDate(endDate: LocalDate) {
        _uiState.update { it.copy(endDate = endDate) }
        reload()
    }

    fun loadExpensesHistory() {
        reload()
    }

    private fun reload() {
        viewModelScope.launch {
            val state = _uiState.value
            val result = getTransactionsByPeriod(start = state.startDate, end = state.endDate)
            updateUiState(result.data)
        }
    }

    private fun updateUiState(data: List<Transaction>) {
        val expenses = data.filter { !it.category.isIncome }
        val total = expenses.sumOf { it.amount }.toFormattedString()
        _uiState.update {
            it.copy(
                summary = ExpensesHistorySumUiState(totalAmount = total),
                items = expenses
                    .sortedByDescending { it.transactionDate }
                    .map { it.asExpensesHistoryItemUiState() }
                    .toImmutableList()
            )
        }
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<ExpensesHistoryViewModel>
}
