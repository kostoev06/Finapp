package com.finapp.feature.settings.passcode

const val PASSCODE_LENGTH = 4

/**
 * Режим экрана код-пароля.
 *
 *  - [SetupNew]     — пользователь устанавливает новый код (затем экран сам перейдёт в [SetupConfirm]).
 *  - [SetupConfirm] — повторный ввод для подтверждения (внутренний шаг, извне не задаётся).
 *  - [Verify]       — проверка существующего кода (например, разблокировка при старте).
 *  - [Disable]      — проверка существующего кода перед удалением.
 */
enum class PasscodeMode {
    SetupNew,
    SetupConfirm,
    Verify,
    Disable
}

data class PasscodeUiState(
    val mode: PasscodeMode = PasscodeMode.Verify,
    val entered: String = "",
    val error: String? = null,
    val finished: Boolean = false
)
