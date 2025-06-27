package com.example.finapp.ui.home.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.finapp.R
import kotlinx.serialization.Serializable

/**
 * Навигационный объект-назначение для главного экрана.
 */
@Serializable
sealed class HomeNavigationDestination(
    @StringRes val titleId: Int,
    @DrawableRes val iconId: Int
) {
    @Serializable
    data object Expenses : HomeNavigationDestination(
        titleId = R.string.expenses_bottom_title,
        iconId = R.drawable.ic_expenses
    )

    @Serializable
    data object Income : HomeNavigationDestination(
        titleId = R.string.income_bottom_title,
        iconId = R.drawable.ic_income
    )

    @Serializable
    data object Account : HomeNavigationDestination(
        titleId = R.string.account_bottom_title,
        iconId = R.drawable.ic_account
    )

    @Serializable
    data object Tags : HomeNavigationDestination(
        titleId = R.string.tags_bottom_title,
        iconId = R.drawable.ic_tags
    )

    @Serializable
    data object Settings : HomeNavigationDestination(
        titleId = R.string.settings_bottom_title,
        iconId = R.drawable.ic_settings
    )

    companion object {
        val destinations = listOf(
            Expenses,
            Income,
            Account,
            Tags,
            Settings
        )
    }
}
