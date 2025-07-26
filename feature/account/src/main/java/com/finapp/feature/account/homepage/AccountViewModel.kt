package com.finapp.feature.account.homepage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finapp.core.common.outcome.handleOutcome
import com.finapp.core.data.api.model.Account
import com.finapp.core.data.api.model.asTransactionInfo
import com.finapp.core.data.api.repository.AccountRepository
import com.finapp.core.data.api.repository.TransactionRepository
import com.finapp.feature.common.di.ViewModelAssistedFactory
import com.finapp.feature.common.utils.toFormattedString
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


sealed class AccountUiEvent {
    data class ShowError(val title: String, val message: String) : AccountUiEvent()
}

private object Keys {
    val LAST_SYNC = longPreferencesKey("last_sync")
}

/**
 * ViewModel для экрана счета.
 */
class AccountViewModel @AssistedInject constructor(
    private val dataStore: DataStore<Preferences>,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountScreenUiState())
    val uiState: StateFlow<AccountScreenUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AccountUiEvent>()
    val events: SharedFlow<AccountUiEvent> = _events.asSharedFlow()

    private val lastSyncInstant: Flow<Instant> = dataStore.data
        .map { prefs -> prefs[Keys.LAST_SYNC] ?: 0L }
        .map { epochSeconds -> Instant.ofEpochSecond(epochSeconds) }

    private val lastSyncTextFlow: Flow<String> = lastSyncInstant.map { instant ->
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
            .withZone(ZoneId.systemDefault())
        formatter.format(instant)
    }

    init {
        viewModelScope.launch {
            lastSyncTextFlow.collect { text ->
                _uiState.update { it.copy(lastSyncTextState = text) }
            }
        }
        loadAccount()
    }

    fun loadAccount() {
        viewModelScope.launch {
            accountRepository.fetchAccount()
                .handleOutcome {
                    onSuccess {
                        updateUiState(data)
                        accountRepository.insertLocalAccount(data)
                    }
                    onFailure {
                        accountRepository.getLocalAccount()?.let { updateUiState(it) }
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
            transactionRepository.fetchTransactionsByPeriod(null, null)
                .handleOutcome {
                    onSuccess {
                        _uiState.update {
                            it.copy(
                                profitItemListUiState = data.toDailyProfitItems().toImmutableList()
                            )
                        }
                        data.forEach { transactionRepository.insertSyncedLocalTransaction(it.asTransactionInfo()) }
                    }
                    onFailure {
                        val todayStart = LocalDate.now().withDayOfMonth(1).atStartOfDay()
                            .atOffset(ZoneOffset.UTC).format(
                                DateTimeFormatter.ISO_OFFSET_DATE_TIME
                            )
                        val todayEnd = LocalDateTime
                            .of(LocalDate.now(), LocalTime.MAX)
                            .atOffset(ZoneOffset.UTC)
                            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                        _uiState.update {
                            it.copy(
                                profitItemListUiState = transactionRepository.getLocalTransactionsByPeriod(
                                    startIso = todayStart,
                                    endIso = todayEnd
                                ).toDailyProfitItems().toImmutableList()
                            )
                        }

                    }
                }
        }
    }

    private fun updateUiState(data: Account) {
        val balance = data.balance.toFormattedString()
        _uiState.update {
            it.copy(
                balanceState = BalanceItemUiState(balance),
                currencyState = CurrencyItemUiState(data.currency)
            )
        }
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<AccountViewModel>
}
