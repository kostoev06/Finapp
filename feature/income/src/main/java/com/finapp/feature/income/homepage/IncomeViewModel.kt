package com.finapp.feature.income.homepage

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
 * ViewModel для экрана доходов.
 */
class IncomeViewModel @AssistedInject constructor(
    private val transactionRepository: TransactionRepository,
    private val currencyRepository: CurrencyRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(IncomeScreenUiState())
    val uiState: StateFlow<IncomeScreenUiState> = _uiState.asStateFlow()

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
        loadIncome()
    }

    fun loadIncome() {
        viewModelScope.launch {
            val today = LocalDate.now().toString()

            transactionRepository.fetchTransactionsByPeriod(startDate = today, endDate = today)
                .handleOutcome {
                    onSuccess {
                        val transactionsSum = data
                            .filter { transaction -> transaction.category.isIncome }
                            .sumOf { it.amount }
                            .toFormattedString()
                        _uiState.update {
                            it.copy(
                                summary = IncomeSumUiState(totalAmount = transactionsSum),
                                items = data
                                    .filter { transaction -> transaction.category.isIncome }
                                    .map { transaction -> transaction.asIncomeItemUiState() }
                                    .toImmutableList()
                            )
                        }
                    }
                }
        }
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<IncomeViewModel>
}
