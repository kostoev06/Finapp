package com.example.finapp.ui.feature.expenses.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.finapp.ui.feature.expenses.ExpensesRoute
import com.example.finapp.ui.feature.expenses.history.ExpensesHistoryRoute
import com.example.finapp.ui.feature.home.navigation.HomeNavigationDestination

inline fun <reified T : HomeNavigationDestination> NavGraphBuilder.expensesNavigation(
    navController: NavController
) {
    composable<ExpensesNavigationDestination.ExpensesHistory> {
        ExpensesHistoryRoute(onClickBack = { navController.popBackStack() })
    }
    composable<T> {
        ExpensesRoute(onClickHistory = { navController.navigate(ExpensesNavigationDestination.ExpensesHistory) })
    }
}
