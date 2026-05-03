package com.finapp.feature.settings.passcode

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.finapp.core.settings.api.repository.PasscodeRepository
import com.finapp.feature.common.di.ViewModelAssistedFactory
import com.finapp.feature.settings.R
import com.finapp.feature.settings.navigation.PasscodeNavMode
import com.finapp.feature.settings.navigation.SettingsNavigationDestination
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

    // При входе через NavHost берём режим из nav-аргумента; если VM создаётся вне навигации
    // (например, экран блокировки на старте приложения) — по умолчанию Verify.
    private val initialMode: PasscodeMode =
        runCatching {
            savedStateHandle.toRoute<SettingsNavigationDestination.Passcode>().mode.toUiMode()
        }.getOrDefault(PasscodeMode.Verify)

    private val _uiState = MutableStateFlow(PasscodeUiState(mode = initialMode))
    val uiState: StateFlow<PasscodeUiState> = _uiState.asStateFlow()

    private var setupBuffer: String = ""

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

private fun PasscodeNavMode.toUiMode(): PasscodeMode = when (this) {
    PasscodeNavMode.SETUP_NEW -> PasscodeMode.SetupNew
    PasscodeNavMode.VERIFY -> PasscodeMode.Verify
    PasscodeNavMode.DISABLE -> PasscodeMode.Disable
}
