package com.finapp.finapp.lock

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finapp.core.settings.api.repository.PasscodeRepository
import com.finapp.feature.common.di.ViewModelAssistedFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn

/**
 * Состояние блокировки приложения.
 *
 *  - [Loading]     — флаг наличия пасскода ещё не получен из DataStore.
 *  - [NotRequired] — пасскод не установлен, экран сразу разблокирован.
 *  - [Required]    — пасскод установлен, нужен ввод.
 *  - [Unlocked]    — пользователь успешно разблокировал в этой сессии.
 */
sealed interface LockState {
    data object Loading : LockState
    data object NotRequired : LockState
    data object Required : LockState
    data object Unlocked : LockState
}

class LockViewModel @AssistedInject constructor(
    passcodeRepository: PasscodeRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val unlockedInSession = MutableStateFlow(false)

    init {
        // Любое изменение isSet после старта сессии (установка нового кода или его снятие
        // через настройки) означает, что пользователь сейчас активно работает с приложением —
        // снова требовать ввод кода в этом случае не нужно.
        passcodeRepository.isSet
            .drop(1)
            .onEach { unlockedInSession.value = true }
            .launchIn(viewModelScope)
    }

    val state = combine(passcodeRepository.isSet, unlockedInSession) { isSet, unlocked ->
        when {
            !isSet -> LockState.NotRequired
            unlocked -> LockState.Unlocked
            else -> LockState.Required
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LockState.Loading
    )

    fun markUnlocked() {
        unlockedInSession.value = true
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<LockViewModel>
}
