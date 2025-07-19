package com.finapp.feature.income.navigation

import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.finapp.feature.income.analysis.IncomeAnalysisRoute
import com.finapp.feature.income.homepage.IncomeRoute
import com.finapp.feature.income.di.LocalFeatureIncomeComponentBuilder
import com.finapp.feature.income.edit.IncomeEditRoute
import com.finapp.feature.income.history.IncomeHistoryRoute

inline fun <reified T : Any> NavGraphBuilder.incomeNavigation(
    navController: NavController
) {
    composable<IncomeNavigationDestination.IncomeHistory> {
        val incomeComponentBuilder = LocalFeatureIncomeComponentBuilder.current
        val incomeComponent = remember { incomeComponentBuilder.build() }
        IncomeHistoryRoute(
            viewModel = viewModel(factory = incomeComponent.viewModelFactory()),
            onClickBack = { navController.popBackStack() },
            onEditIncome = { id ->
                navController.navigate(
                    IncomeNavigationDestination.EditIncome(
                        id
                    )
                )
            },
            onAnalysis = { navController.navigate(IncomeNavigationDestination.IncomeAnalysis) }
        )
    }
    composable<IncomeNavigationDestination.IncomeAnalysis> {
        val incomeComponentBuilder = LocalFeatureIncomeComponentBuilder.current
        val incomeComponent = remember { incomeComponentBuilder.build() }
        IncomeAnalysisRoute(
            viewModel = viewModel(factory = incomeComponent.viewModelFactory()),
            onClickBack = { navController.popBackStack() },
        )
    }
    composable<IncomeNavigationDestination.EditIncome> {
        val incomeComponentBuilder = LocalFeatureIncomeComponentBuilder.current
        val incomeComponent = remember { incomeComponentBuilder.build() }
        IncomeEditRoute(
            viewModel = viewModel(factory = incomeComponent.viewModelFactory()),
            popBack = { navController.popBackStack() }
        )
    }
    composable<T> {
        val incomeComponentBuilder = LocalFeatureIncomeComponentBuilder.current
        val incomeComponent = remember { incomeComponentBuilder.build() }
        IncomeRoute(
            viewModel = viewModel(factory = incomeComponent.viewModelFactory()),
            onClickHistory = { navController.navigate(IncomeNavigationDestination.IncomeHistory) },
            onEditIncome = { id ->
                navController.navigate(
                    IncomeNavigationDestination.EditIncome(
                        id
                    )
                )
            })
    }
}
