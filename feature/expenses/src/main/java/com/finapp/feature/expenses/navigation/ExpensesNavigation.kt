package com.finapp.feature.expenses.navigation

import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.finapp.feature.expenses.ExpensesRoute
import com.finapp.feature.expenses.di.LocalFeatureExpensesComponentBuilder
import com.finapp.feature.expenses.edit.ExpenseEditRoute
import com.finapp.feature.expenses.history.ExpensesHistoryRoute

inline fun <reified T : Any> NavGraphBuilder.expensesNavigation(
    navController: NavController
) {
    composable<ExpensesNavigationDestination.ExpensesHistory> {
        val expensesComponentBuilder = LocalFeatureExpensesComponentBuilder.current
        val expensesComponent = remember { expensesComponentBuilder.build() }
        ExpensesHistoryRoute(
            viewModel = viewModel(factory = expensesComponent.viewModelFactory()),
            onClickBack = { navController.popBackStack() },
            onEditIncome = { id ->
                navController.navigate(
                    ExpensesNavigationDestination.EditExpense(
                        id
                    )
                )
            })
    }
    composable<ExpensesNavigationDestination.EditExpense> {
        val expensesComponentBuilder = LocalFeatureExpensesComponentBuilder.current
        val expensesComponent = remember { expensesComponentBuilder.build() }
        ExpenseEditRoute(
            viewModel = viewModel(factory = expensesComponent.viewModelFactory()),
            popBack = { navController.popBackStack() }
        )
    }
    composable<T> {
        val expensesComponentBuilder = LocalFeatureExpensesComponentBuilder.current
        val expensesComponent = remember { expensesComponentBuilder.build() }
        ExpensesRoute(
            viewModel = viewModel(factory = expensesComponent.viewModelFactory()),
            onClickHistory = { navController.navigate(ExpensesNavigationDestination.ExpensesHistory) },
            onEditExpense = { id ->
                navController.navigate(
                    ExpensesNavigationDestination.EditExpense(
                        id
                    )
                )
            }
        )
    }
}
