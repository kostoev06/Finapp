package com.finapp.feature.expenses.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finapp.core.common.outcome.handleOutcome
import com.finapp.core.data.api.model.CurrencyCode
import com.finapp.core.data.api.repository.CurrencyRepository
import com.finapp.core.data.api.repository.TransactionRepository
import com.finapp.feature.common.di.ViewModelAssistedFactory
import com.finapp.feature.common.utils.toFormattedString
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * ViewModel для экрана истории расходов.
 */
class ExpensesHistoryViewModel @AssistedInject constructor(
    private val transactionRepository: TransactionRepository,
    private val currencyRepository: CurrencyRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpensesHistoryScreenUiState())
    val uiState: StateFlow<ExpensesHistoryScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            currencyRepository.currency.collect { currency ->
                _uiState.update {
                    it.copy(
                        currency = CurrencyCode.from(currency)
                    )
                }
            }
        }
        loadExpensesHistory()
    }

    fun onChooseStartDate(startDate: LocalDate) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(startDate = startDate)
            }
            fetchTransactions()
        }
    }

    fun onChooseEndDate(endDate: LocalDate) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(endDate = endDate)
            }
            fetchTransactions()
        }
    }

    fun loadExpensesHistory() {
        viewModelScope.launch {
            fetchTransactions()
        }
    }

    private suspend fun fetchTransactions() {
        transactionRepository.fetchTransactionsByPeriod(
            startDate = _uiState.value.startDate.toString(),
            endDate = _uiState.value.endDate.toString()
        ).handleOutcome {
            onSuccess {
                val transactionsSum = data
                    .filter { transaction -> !transaction.category.isIncome }
                    .sumOf { it.amount }
                    .toFormattedString()
                _uiState.update {
                    it.copy(
                        summary = ExpensesHistorySumUiState(totalAmount = transactionsSum),
                        items = data
                            .filter { transaction -> !transaction.category.isIncome }
                            .sortedByDescending { transaction -> transaction.transactionDate }
                            .map { transaction -> transaction.asExpensesHistoryItemUiState() }.toImmutableList()
                    )
                }
            }
        }
    }


    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<ExpensesHistoryViewModel>
}
