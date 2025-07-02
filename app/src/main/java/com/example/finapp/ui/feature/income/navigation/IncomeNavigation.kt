package com.example.finapp.ui.feature.income.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.finapp.ui.feature.income.history.IncomeHistoryRoute
import com.example.finapp.ui.feature.income.IncomeRoute
import com.example.finapp.ui.feature.home.navigation.HomeNavigationDestination

inline fun <reified T : HomeNavigationDestination> NavGraphBuilder.incomeNavigation(
    navController: NavController
) {
    composable<IncomeNavigationDestination.IncomeHistory> {
        IncomeHistoryRoute(onClickBack = { navController.popBackStack() })
    }
    composable<T> {
        IncomeRoute(onClickHistory = { navController.navigate(IncomeNavigationDestination.IncomeHistory) })
    }
}
