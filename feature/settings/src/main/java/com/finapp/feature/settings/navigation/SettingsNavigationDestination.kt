package com.finapp.feature.settings.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class SettingsNavigationDestination {
    @Serializable
    data object BrandColorPicker : SettingsNavigationDestination()
    @Serializable
    data object About : SettingsNavigationDestination()
}
