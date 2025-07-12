package com.finapp.feature.home.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.finapp.feature.account.navigation.accountNavigation
import com.finapp.feature.expenses.navigation.expensesNavigation
import com.finapp.feature.income.navigation.incomeNavigation
import com.finapp.feature.settings.SettingsRoute
import com.finapp.feature.tags.navigation.tagsNavigation

@Composable
fun HomeNavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = HomeNavigationDestination.Account //replace to Expenses
    ) {
        expensesNavigation<HomeNavigationDestination.Expenses>(navController)

        incomeNavigation<HomeNavigationDestination.Income>(navController)

        accountNavigation<HomeNavigationDestination.Account>(navController)

        tagsNavigation<HomeNavigationDestination.Tags>(navController)

        composable<HomeNavigationDestination.Settings> {
            SettingsRoute()
        }
    }
}