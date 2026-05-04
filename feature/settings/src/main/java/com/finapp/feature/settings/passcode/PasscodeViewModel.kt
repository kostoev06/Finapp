package com.finapp.feature.settings.passcode

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finapp.core.settings.api.repository.PasscodeRepository
import com.finapp.feature.common.di.ViewModelAssistedFactory
import com.finapp.feature.settings.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PasscodeViewModel @AssistedInject constructor(
    private val passcodeRepository: PasscodeRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Стартовый режим — Verify; Route однократно вызовет [ensureMode] и установит нужный.
    // Так VM не дёргает nav-route и не зависит от того, как создаётся (через NavHost
    // или из MainActivity для экрана блокировки).
    private val _uiState = MutableStateFlow(PasscodeUiState(mode = PasscodeMode.Verify))
    val uiState: StateFlow<PasscodeUiState> = _uiState.asStateFlow()

    private var modeInitialized = false
    private var setupBuffer: String = ""

    /**
     * Выставляет начальный режим один раз за жизнь VM. Повторные вызовы игнорируются —
     * иначе при поворотах/рекомпозициях экран бы скакал между режимами SetupNew/SetupConfirm.
     */
    fun ensureMode(mode: PasscodeMode) {
        if (modeInitialized) return
        modeInitialized = true
        _uiState.update { it.copy(mode = mode) }
    }

    fun onDigit(digit: Char) {
        val current = _uiState.value
        if (current.finished || current.entered.length >= PASSCODE_LENGTH) return
        val next = current.entered + digit
        _uiState.update { it.copy(entered = next, errorRes = null) }
        if (next.length == PASSCODE_LENGTH) {
            handleComplete(next)
        }
    }

    fun onBackspace() {
        _uiState.update {
            if (it.finished || it.entered.isEmpty()) it
            else it.copy(entered = it.entered.dropLast(1), errorRes = null)
        }
    }

    private fun handleComplete(code: String) {
        when (_uiState.value.mode) {
            PasscodeMode.SetupNew -> {
                setupBuffer = code
                _uiState.update {
                    it.copy(mode = PasscodeMode.SetupConfirm, entered = "", errorRes = null)
                }
            }
            PasscodeMode.SetupConfirm -> {
                if (code == setupBuffer) {
                    viewModelScope.launch {
                        passcodeRepository.set(code)
                        _uiState.update { it.copy(finished = true) }
                    }
                } else {
                    setupBuffer = ""
                    _uiState.update {
                        it.copy(
                            mode = PasscodeMode.SetupNew,
                            entered = "",
                            errorRes = R.string.passcode_error_mismatch
                        )
                    }
                }
            }
            PasscodeMode.Verify -> {
                viewModelScope.launch {
                    if (passcodeRepository.verify(code)) {
                        _uiState.update { it.copy(finished = true) }
                    } else {
                        _uiState.update { it.copy(entered = "", errorRes = R.string.passcode_error_wrong) }
                    }
                }
            }
            PasscodeMode.Disable -> {
                viewModelScope.launch {
                    if (passcodeRepository.verify(code)) {
                        passcodeRepository.clear()
                        _uiState.update { it.copy(finished = true) }
                    } else {
                        _uiState.update { it.copy(entered = "", errorRes = R.string.passcode_error_wrong) }
                    }
                }
            }
        }
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<PasscodeViewModel>
}
