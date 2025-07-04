package com.example.finapp.ui.feature.account.edit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finapp.data.common.handleOutcome
import com.example.finapp.data.repository.AccountRepository
import com.example.finapp.data.repository.CurrencyRepository
import com.example.finapp.data.repository.impl.CurrencyRepositoryImpl
import com.example.finapp.ui.feature.account.edit.BalanceFieldUiState
import com.example.finapp.ui.feature.account.edit.CurrencyFieldUiState
import com.example.finapp.ui.feature.account.edit.EditAccountScreenUiState
import com.example.finapp.ui.feature.account.edit.NameFieldUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class EditAccountUiEvent {
    data class ShowError(val title: String, val message: String) : EditAccountUiEvent()
    data object OnSaveSuccess : EditAccountUiEvent()
}

/**
 * ViewModel для экрана изменения счета.
 */
class EditAccountViewModel(
    private val accountRepository: AccountRepository,
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditAccountScreenUiState())
    val uiState: StateFlow<EditAccountScreenUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<EditAccountUiEvent>()
    val events: SharedFlow<EditAccountUiEvent> = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            accountRepository.getAccount()
                .handleOutcome {
                    onSuccess {
                        val balance = data.balance.toPlainString()
                        _uiState.update {
                            it.copy(
                                nameFieldState = NameFieldUiState(data.name),
                                balanceFieldState = BalanceFieldUiState(balance),
                                currencyFieldState = CurrencyFieldUiState(data.currency)
                            )
                        }
                    }
                    onFailure {
                        onError {
                            _events.emit(
                                EditAccountUiEvent.ShowError(
                                    title = "Ошибка $code",
                                    message = errorBody ?: "Неизвестная ошибка"
                                )
                            )
                        }
                        onException {
                            _events.emit(
                                EditAccountUiEvent.ShowError(
                                    title = "Ошибка",
                                    message = "Неизвестная ошибка"
                                )
                            )
                        }
                    }
                }
        }
    }

    fun onNameChange(new: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(nameFieldState = it.nameFieldState.copy(text = new))
            }
        }
    }

    fun onBalanceChange(new: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(balanceFieldState = it.balanceFieldState.copy(text = new))
            }
        }
    }

    fun onCurrencyClick() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isCurrencySheetVisible = true)
            }
        }
    }

    fun onCurrencySelect(currency: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    currencyFieldState = it.currencyFieldState.copy(currency = currency),
                    isCurrencySheetVisible = false
                )
            }
        }
    }

    fun onCancelSheet() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isCurrencySheetVisible = false)
            }
        }
    }

    fun onSave() {
        viewModelScope.launch {
            accountRepository.updateAccount(
                _uiState.value.nameFieldState.text,
                _uiState.value.balanceFieldState.text,
                _uiState.value.currencyFieldState.currency
            ).handleOutcome {
                onSuccess {
                    _events.emit(EditAccountUiEvent.OnSaveSuccess)
                    currencyRepository.setCurrency(_uiState.value.currencyFieldState.currency)
                }
                onFailure {
                    onError {
                        _events.emit(
                            EditAccountUiEvent.ShowError(
                                title = "Ошибка $code",
                                message = errorBody ?: "Неизвестная ошибка"
                            )
                        )
                    }
                    onException {
                        _events.emit(
                            EditAccountUiEvent.ShowError(
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