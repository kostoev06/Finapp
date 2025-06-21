package com.example.finapp.ui.home.expenses.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.finapp.ui.home.expenses.component.ExpensesRoute
import com.example.finapp.ui.home.expenses.history.component.ExpensesHistoryRoute
import com.example.finapp.ui.home.navigation.HomeNavigationDestination

inline fun <reified T: HomeNavigationDestination> NavGraphBuilder.expensesNavigation(
    navController: NavController
) {
    composable<ExpensesNavigationDestination.ExpensesHistory> {
        ExpensesHistoryRoute(onClickBack = { navController.popBackStack() })
    }
    composable<T> {
        ExpensesRoute(onClickHistory = { navController.navigate(ExpensesNavigationDestination.ExpensesHistory) })
    }
}