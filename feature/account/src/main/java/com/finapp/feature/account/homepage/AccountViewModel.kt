package com.finapp.feature.account.homepage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finapp.core.data.api.model.Account
import com.finapp.core.data.api.repository.SyncStatusRepository
import com.finapp.core.domain.usecase.GetAccountUseCase
import com.finapp.core.domain.usecase.GetTransactionsByPeriodUseCase
import com.finapp.feature.common.di.ViewModelAssistedFactory
import com.finapp.feature.common.text.UiText
import com.finapp.feature.common.utils.toErrorTexts
import com.finapp.feature.common.utils.toFormattedString
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


sealed class AccountUiEvent {
    data class ShowError(val title: UiText, val message: UiText) : AccountUiEvent()
}

/**
 * ViewModel для экрана счета.
 */
class AccountViewModel @AssistedInject constructor(
    private val getAccount: GetAccountUseCase,
    private val getTransactionsByPeriod: GetTransactionsByPeriodUseCase,
    syncStatusRepository: SyncStatusRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountScreenUiState())
    val uiState: StateFlow<AccountScreenUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AccountUiEvent>()
    val events: SharedFlow<AccountUiEvent> = _events.asSharedFlow()

    private val lastSyncFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        .withZone(ZoneId.systemDefault())

    init {
        // null = синхронизаций ещё не было — оставляем пустую строку, а не «01.01.1970 …».
        syncStatusRepository.lastSyncEpoch
            .map { epoch -> epoch?.let { lastSyncFormatter.format(Instant.ofEpochSecond(it)) }.orEmpty() }
            .onEach { text -> _uiState.update { it.copy(lastSyncTextState = text) } }
            .launchIn(viewModelScope)
        loadAccount()
    }

    fun loadAccount() {
        viewModelScope.launch {
            val accountResult = getAccount()
            accountResult.data?.let { updateUiState(it) }
            accountResult.error?.let { failure ->
                val (title, message) = failure.toErrorTexts()
                _events.emit(AccountUiEvent.ShowError(title, message))
            }

            // Дневной profit-график на экране — за текущий месяц. Сюда же запрашиваем у бэкенда
            // (раньше тянули всю историю — лишний трафик и асимметрия с локальным фолбэком).
            val today = LocalDate.now()
            val periodResult = getTransactionsByPeriod(
                start = today.withDayOfMonth(1),
                end = today
            )
            _uiState.update {
                it.copy(profitItemListUiState = periodResult.data.toDailyProfitItems().toImmutableList())
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
