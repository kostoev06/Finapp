package com.example.finapp.ui.home.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.finapp.ui.home.account.component.AccountRoute
import com.example.finapp.ui.home.expenses.navigation.expensesNavigation
import com.example.finapp.ui.home.income.navigation.incomeNavigation
import com.example.finapp.ui.home.settings.component.SettingsRoute
import com.example.finapp.ui.home.tags.component.TagsRoute

@Composable
fun HomeNavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = HomeNavigationDestination.Expenses
    ) {
        expensesNavigation<HomeNavigationDestination.Expenses>(navController)

        incomeNavigation<HomeNavigationDestination.Income>(navController)

        composable<HomeNavigationDestination.Account> {
            AccountRoute()
        }

        composable<HomeNavigationDestination.Tags> {
            TagsRoute()
        }

        composable<HomeNavigationDestination.Settings> {
            SettingsRoute()
        }
    }
}