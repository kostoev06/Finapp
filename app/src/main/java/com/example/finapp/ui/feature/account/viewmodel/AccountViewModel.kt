package com.example.finapp.ui.feature.account.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finapp.data.common.handleOutcome
import com.example.finapp.data.repository.AccountRepository
import com.example.finapp.ui.feature.account.AccountScreenUiState
import com.example.finapp.ui.feature.account.BalanceItemUiState
import com.example.finapp.ui.feature.account.CurrencyItemUiState
import com.example.finapp.ui.feature.account.edit.viewmodel.EditAccountUiEvent
import com.example.finapp.ui.utils.toFormattedString
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


sealed class AccountUiEvent {
    data class ShowError(val title: String, val message: String) : AccountUiEvent()
}

/**
 * ViewModel для экрана счета.
 */
class AccountViewModel(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountScreenUiState())
    val uiState: StateFlow<AccountScreenUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AccountUiEvent>()
    val events: SharedFlow<AccountUiEvent> = _events.asSharedFlow()

    init {
        loadAccount()
    }

    fun loadAccount() {
        viewModelScope.launch {
            accountRepository.getAccount()
                .handleOutcome {
                    onSuccess {
                        val balance = data.balance.toFormattedString()
                        _uiState.update {
                            it.copy(
                                balanceState = BalanceItemUiState(balance),
                                currencyState = CurrencyItemUiState(data.currency)
                            )
                        }
                    }
                    onFailure {
                        onError {
                            _events.emit(
                                AccountUiEvent.ShowError(
                                    title = "Ошибка $code",
                                    message = errorBody ?: "Неизвестная ошибка"
                                )
                            )
                        }
                        onException {
                            _events.emit(
                                AccountUiEvent.ShowError(
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
