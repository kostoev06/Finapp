package com.finapp.feature.expenses.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.finapp.core.common.outcome.Outcome
import com.finapp.core.common.outcome.handleOutcome
import com.finapp.core.data.api.repository.AccountRepository
import com.finapp.core.data.api.repository.CategoryRepository
import com.finapp.core.data.api.repository.TransactionRepository
import com.finapp.feature.common.di.ViewModelAssistedFactory
import com.finapp.feature.common.utils.toFormattedString
import com.finapp.feature.expenses.navigation.ExpensesNavigationDestination
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
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


sealed class ExpenseEditUiEvent {
    data class ShowError(val title: String, val message: String) : ExpenseEditUiEvent()
    data object OnSaveSuccess : ExpenseEditUiEvent()
}

class ExpenseEditViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {
    private val expenseId = savedStateHandle.toRoute<ExpensesNavigationDestination.EditExpense>().expenseId

    private val _uiState = MutableStateFlow(
        ExpenseEditScreenUiState(
            expenseId = expenseId,
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
    val uiState: StateFlow<ExpenseEditScreenUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ExpenseEditUiEvent>()
    val events: SharedFlow<ExpenseEditUiEvent> = _events.asSharedFlow()

    init {
        if (expenseId != null) {
            viewModelScope.launch {
                transactionRepository.getTransactionById(expenseId)
                    .handleOutcome {
                        onSuccess {
                            _uiState.update {
                                it.copy(
                                    currentCategoryState = data.category.asCategoryUiState(),
                                    amountFieldState = data.amount.toString(),
                                    dateState = data.transactionDate.toLocalDate(),
                                    timeState = data.transactionDate.toLocalTime(),
                                    commentFieldState = data.comment ?: ""
                                )
                            }
                        }
                        onFailure {
                            onError {
                                _events.emit(
                                    ExpenseEditUiEvent.ShowError(
                                        title = "Ошибка $code",
                                        message = errorBody ?: "Неизвестная ошибка"
                                    )
                                )
                            }
                            onException {
                                _events.emit(
                                    ExpenseEditUiEvent.ShowError(
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
                when (val accountOutcome = accountRepository.getAccount()) {
                    is Outcome.Success -> accountOutcome.data.name
                    else -> {
                        _events.emit(
                            ExpenseEditUiEvent.ShowError(
                                title = "Ошибка",
                                message = "Неизвестная ошибка"
                            )
                        )
                        ""
                    }
                }
            categoryRepository.getCategoriesByType(isIncome = false).handleOutcome {
                onSuccess {
                    _uiState.update {
                        it.copy(
                            accountFieldState = accountName,
                            categoriesListState = data.map { category -> category.asCategoryUiState() }
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
            val localDateTime = LocalDateTime.of(
                _uiState.value.dateState,
                _uiState.value.timeState
            )
            val offsetDateTime = localDateTime.atOffset(ZoneOffset.UTC)
            val isoTime = offsetDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

            val result = if (expenseId == null) {
                transactionRepository.createTransaction(
                    categoryId = _uiState.value.currentCategoryState.id,
                    amount = _uiState.value.amountFieldState,
                    transactionDateIso = isoTime,
                    comment = _uiState.value.commentFieldState
                )
            } else {
                transactionRepository.updateTransaction(
                    id = expenseId,
                    categoryId = _uiState.value.currentCategoryState.id,
                    amount = _uiState.value.amountFieldState,
                    transactionDateIso = isoTime,
                    comment = _uiState.value.commentFieldState
                )
            }

            result.handleOutcome {
                onSuccess {
                    _events.emit(ExpenseEditUiEvent.OnSaveSuccess)
                }
                onFailure {
                    onError {
                        _events.emit(
                            ExpenseEditUiEvent.ShowError(
                                title = "Ошибка $code",
                                message = errorBody ?: "Неизвестная ошибка"
                            )
                        )
                    }
                    onException {
                        _events.emit(
                            ExpenseEditUiEvent.ShowError(
                                title = "Ошибка",
                                message = "Неизвестная ошибка"
                            )
                        )
                    }
                }
            }
        }
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<ExpenseEditViewModel>
}