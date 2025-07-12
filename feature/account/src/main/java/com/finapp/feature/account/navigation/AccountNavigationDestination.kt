package com.finapp.feature.account.navigation

import kotlinx.serialization.Serializable

/**
 * Навигационный объект-назначение для экрана счета.
 */
@Serializable
sealed class AccountNavigationDestination {
    @Serializable
    data object EditAccount : AccountNavigationDestination()
}

