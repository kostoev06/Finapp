package com.finapp.feature.income.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.finapp.core.common.outcome.Outcome
import com.finapp.core.common.outcome.handleOutcome
import com.finapp.core.data.api.model.Transaction
import com.finapp.core.data.api.model.TransactionBrief
import com.finapp.core.data.api.model.TransactionInfo
import com.finapp.core.data.api.model.asTransactionInfo
import com.finapp.core.data.api.repository.AccountRepository
import com.finapp.core.data.api.repository.CategoryRepository
import com.finapp.core.data.api.repository.TransactionRepository
import com.finapp.feature.common.di.ViewModelAssistedFactory
import com.finapp.feature.income.navigation.IncomeNavigationDestination
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


sealed class IncomeEditUiEvent {
    data class ShowError(val title: String, val message: String) : IncomeEditUiEvent()
    data object OnSaveSuccess : IncomeEditUiEvent()
}

class IncomeEditViewModel @AssistedInject constructor(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val incomeId = savedStateHandle.toRoute<IncomeNavigationDestination.EditIncome>().incomeId

    private val _uiState = MutableStateFlow(
        IncomeEditScreenUiState(
            incomeId = incomeId,
            accountFieldState = "",
            currentCategoryState = CategoryUiState(0, ""),
            categoriesListState = persistentListOf(),
            isCategoryDialogVisible = false,
            amountFieldState = "",
            dateState = LocalDate.now(),
            timeState = LocalTime.now(),
            commentFieldState = ""
        )
    )
    val uiState: StateFlow<IncomeEditScreenUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<IncomeEditUiEvent>()
    val events: SharedFlow<IncomeEditUiEvent> = _events.asSharedFlow()

    init {
        if (incomeId != null) {
            viewModelScope.launch {
                transactionRepository.fetchTransactionById(incomeId)
                    .handleOutcome {
                        onSuccess {
                            updateUiState(data)
                            transactionRepository.insertSyncedLocalTransaction(data.asTransactionInfo())
                        }
                        onFailure {
                            val localData = transactionRepository.getLocalTransactionById(incomeId)
                            if (localData != null) {
                                updateUiState(localData)
                            }

                            onError {
                                _events.emit(
                                    IncomeEditUiEvent.ShowError(
                                        title = "Ошибка $code",
                                        message = errorBody ?: "Неизвестная ошибка"
                                    )
                                )
                            }
                            onException {
                                _events.emit(
                                    IncomeEditUiEvent.ShowError(
                                        title = "Ошибка",
                                        message = "Неизвестная ошибка"
                                    )
                                )
                            }
                        }
                    }
            }
        }
        viewModelScope.launch {
            val accountName: String =
                when (val accountOutcome = accountRepository.fetchAccount()) {
                    is Outcome.Success -> accountOutcome.data.name
                    else -> {
                        _events.emit(
                            IncomeEditUiEvent.ShowError(
                                title = "Ошибка",
                                message = "Неизвестная ошибка"
                            )
                        )
                        accountRepository.getLocalAccount()?.name ?: ""
                    }
                }
            categoryRepository.fetchCategoriesByType(isIncome = true).handleOutcome {
                onSuccess {
                    _uiState.update {
                        it.copy(
                            accountFieldState = accountName,
                            categoriesListState = data.map { category -> category.asCategoryUiState() }
                                .toImmutableList()
                        )
                    }
                }
                onFailure {
                    val localData = categoryRepository.getLocalCategoriesByType(isIncome = true)
                    _uiState.update {
                        it.copy(
                            accountFieldState = accountName,
                            categoriesListState = localData.map { category -> category.asCategoryUiState() }
                                .toImmutableList()
                        )
                    }
                }
            }
        }
    }

    fun onAmountChange(new: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(amountFieldState = new)
            }
        }
    }

    fun onCategoryClick() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isCategoryDialogVisible = true)
            }
        }
    }

    fun onCategorySelect(categoryId: Long) {
        viewModelScope.launch {
            _uiState.update { state ->
                val picked = state.categoriesListState.firstOrNull { it.id == categoryId }
                    ?: return@update state

                state.copy(
                    currentCategoryState = picked,
                    isCategoryDialogVisible = false
                )
            }
        }
    }

    fun onCancelDialog() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isCategoryDialogVisible = false)
            }
        }
    }

    fun onChooseDate(date: LocalDate) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(dateState = date)
            }
        }
    }

    fun onChooseTime(time: LocalTime) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(timeState = time)
            }
        }
    }

    fun onCommentChange(new: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(commentFieldState = new)
            }
        }
    }

    fun onSave() {
        viewModelScope.launch {
            if (_uiState.value.amountFieldState.isEmpty()) {
                _events.emit(
                    IncomeEditUiEvent.ShowError(
                        title = "Ошибка",
                        message = "Неверный формат суммы"
                    )
                )
            } else {
                val localDateTime = LocalDateTime.of(
                    _uiState.value.dateState,
                    _uiState.value.timeState
                )
                val accountData = accountRepository.getLocalAccount()

                if (incomeId == null) {
                    transactionRepository.createTransaction(
                        TransactionBrief(
                            id = null,
                            categoryId = _uiState.value.currentCategoryState.id,
                            amount = _uiState.value.amountFieldState.toBigDecimal(),
                            transactionDate = localDateTime,
                            comment = _uiState.value.commentFieldState
                        )
                    ).handleOutcome {
                        onSuccess {
                            transactionRepository.insertSyncedLocalTransaction(data)
                            _events.emit(IncomeEditUiEvent.OnSaveSuccess)
                        }
                        onFailure {
                            transactionRepository.insertUnsyncedLocalTransaction(
                                TransactionInfo(
                                    id = null,
                                    accountId = accountData!!.id,
                                    categoryId = _uiState.value.currentCategoryState.id,
                                    amount = _uiState.value.amountFieldState.toBigDecimal(),
                                    transactionDate = localDateTime,
                                    comment = _uiState.value.commentFieldState,
                                    createdAt = localDateTime,
                                    updatedAt = localDateTime
                                )
                            )
                            _events.emit(IncomeEditUiEvent.OnSaveSuccess)
                            onError {
                                _events.emit(
                                    IncomeEditUiEvent.ShowError(
                                        title = "Ошибка $code",
                                        message = errorBody ?: "Неизвестная ошибка"
                                    )
                                )
                            }
                            onException {
                                _events.emit(
                                    IncomeEditUiEvent.ShowError(
                                        title = "Ошибка",
                                        message = "Неизвестная ошибка"
                                    )
                                )
                            }
                        }
                    }
                } else {
                    transactionRepository.updateTransaction(
                        TransactionBrief(
                            id = incomeId,
                            categoryId = _uiState.value.currentCategoryState.id,
                            amount = _uiState.value.amountFieldState.toBigDecimal(),
                            transactionDate = localDateTime,
                            comment = _uiState.value.commentFieldState
                        )
                    ).handleOutcome {
                        onSuccess {
                            transactionRepository.updateSyncedLocalTransaction(data.asTransactionInfo())
                            _events.emit(IncomeEditUiEvent.OnSaveSuccess)
                        }
                        onFailure {
                            if (transactionRepository.getSyncedLocalTransactionById(incomeId) != null) {
                                transactionRepository.updateSyncedLocalTransaction(
                                    TransactionInfo(
                                        id = incomeId,
                                        accountId = accountData!!.id,
                                        categoryId = _uiState.value.currentCategoryState.id,
                                        amount = _uiState.value.amountFieldState.toBigDecimal(),
                                        transactionDate = localDateTime,
                                        comment = _uiState.value.commentFieldState,
                                        createdAt = localDateTime,
                                        updatedAt = localDateTime
                                    )
                                )
                            } else {
                                transactionRepository.updateUnsyncedLocalTransaction(
                                    TransactionInfo(
                                        id = incomeId,
                                        accountId = accountData!!.id,
                                        categoryId = _uiState.value.currentCategoryState.id,
                                        amount = _uiState.value.amountFieldState.toBigDecimal(),
                                        transactionDate = localDateTime,
                                        comment = _uiState.value.commentFieldState,
                                        createdAt = localDateTime,
                                        updatedAt = localDateTime
                                    )
                                )
                            }

                            _events.emit(IncomeEditUiEvent.OnSaveSuccess)
                            onError {
                                _events.emit(
                                    IncomeEditUiEvent.ShowError(
                                        title = "Ошибка $code",
                                        message = errorBody ?: "Неизвестная ошибка"
                                    )
                                )
                            }
                            onException {
                                _events.emit(
                                    IncomeEditUiEvent.ShowError(
                                        title = "Ошибка",
                                        message = "Неизвестная ошибка"
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateUiState(data: Transaction) {
        _uiState.update {
            it.copy(
                currentCategoryState = data.category.asCategoryUiState(),
                amountFieldState = data.amount.toString(),
                dateState = data.updatedAt.toLocalDate(),
                timeState = data.updatedAt.toLocalTime(),
                commentFieldState = data.comment ?: ""
            )
        }
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<IncomeEditViewModel>
}