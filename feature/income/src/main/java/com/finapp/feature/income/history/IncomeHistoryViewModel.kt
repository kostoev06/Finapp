package com.finapp.feature.income.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finapp.core.common.outcome.handleOutcome
import com.finapp.core.data.api.model.CurrencyCode
import com.finapp.core.data.api.model.Transaction
import com.finapp.core.data.api.model.asTransactionInfo
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
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * ViewModel для экрана истории доходов.
 */
class IncomeHistoryViewModel @AssistedInject constructor(
    private val transactionRepository: TransactionRepository,
    private val currencyRepository: CurrencyRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(IncomeHistoryScreenUiState())
    val uiState: StateFlow<IncomeHistoryScreenUiState> = _uiState.asStateFlow()

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
        loadIncomeHistory()
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

    fun loadIncomeHistory() {
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
                updateUiState(data)
                data.forEach { transactionRepository.insertSyncedLocalTransaction(it.asTransactionInfo()) }
            }
            onFailure {
                val todayStart = _uiState.value.startDate.atStartOfDay().atOffset(ZoneOffset.UTC).format(
                    DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                val todayEnd = LocalDateTime
                    .of(_uiState.value.endDate, LocalTime.MAX)
                    .atOffset(ZoneOffset.UTC)
                    .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                updateUiState(transactionRepository.getLocalTransactionsByPeriod(startIso = todayStart, endIso = todayEnd))
            }
        }
    }

    private fun updateUiState(data: List<Transaction>) {
        val transactionsSum = data
            .filter { transaction -> transaction.category.isIncome }
            .sumOf { it.amount }
            .toFormattedString()
        _uiState.update {
            it.copy(
                summary = IncomeHistorySumUiState(totalAmount = transactionsSum),
                items = data
                    .filter { transaction -> transaction.category.isIncome }
                    .sortedByDescending { transaction -> transaction.transactionDate }
                    .map { transaction -> transaction.asIncomeHistoryItemUiState() }.toImmutableList()
            )
        }
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<IncomeHistoryViewModel>
}
