package com.example.finapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.finapp.home.account.view.AccountRoute
import com.example.finapp.home.expenses.view.ExpensesRoute
import com.example.finapp.home.income.view.IncomeRoute
import com.example.finapp.home.settings.view.SettingsRoute
import com.example.finapp.home.tags.view.TagsRoute

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
        composable<HomeNavigationDestination.Expenses> {
            ExpensesRoute()
        }

        composable<HomeNavigationDestination.Income> {
            IncomeRoute()
        }

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