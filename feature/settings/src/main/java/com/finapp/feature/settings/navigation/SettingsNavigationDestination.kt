package com.finapp.feature.settings.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class SettingsNavigationDestination {
    @Serializable
    data object BrandColorPicker : SettingsNavigationDestination()
    @Serializable
    data object About : SettingsNavigationDestination()

    /**
     * Экран код-пароля. [mode] определяет начальный режим:
     * `SETUP_NEW` — установка нового, `VERIFY` — проверка, `DISABLE` — снятие.
     */
    @Serializable
    data class Passcode(val mode: PasscodeNavMode) : SettingsNavigationDestination()
}

@Serializable
enum class PasscodeNavMode { SETUP_NEW, VERIFY, DISABLE }
