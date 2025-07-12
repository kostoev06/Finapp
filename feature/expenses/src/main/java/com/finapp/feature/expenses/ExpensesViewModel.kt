package com.finapp.feature.expenses

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
 * ViewModel для экрана расходов.
 */
class ExpensesViewModel @AssistedInject constructor(
    private val transactionRepository: TransactionRepository,
    private val currencyRepository: CurrencyRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExpensesScreenUiState())
    val uiState: StateFlow<ExpensesScreenUiState> = _uiState.asStateFlow()

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
        loadExpenses()
    }

    fun loadExpenses() {
        viewModelScope.launch {
            val today = LocalDate.now().toString()

            transactionRepository.getTransactionsByPeriod(startDate = today, endDate = today)
                .handleOutcome {
                    onSuccess {
                        val transactionsSum = data
                            .filter { transaction -> !transaction.category.isIncome }
                            .sumOf { it.amount }
                            .toFormattedString()
                        _uiState.update {
                            it.copy(
                                summary = ExpensesSumUiState(totalAmount = transactionsSum),
                                items = data
                                    .filter { transaction -> !transaction.category.isIncome }
                                    .map { transaction -> transaction.asExpensesItemUiState() }
                                    .toImmutableList()
                            )
                        }
                    }
                }
        }
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<ExpensesViewModel>
}
