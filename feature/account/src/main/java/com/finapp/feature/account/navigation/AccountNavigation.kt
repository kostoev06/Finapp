package com.finapp.feature.account.navigation

import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.finapp.feature.account.homepage.AccountRoute
import com.finapp.feature.account.di.LocalFeatureAccountComponentBuilder
import com.finapp.feature.account.edit.AccountEditRoute


inline fun <reified T : Any> NavGraphBuilder.accountNavigation(
    navController: NavController
) {
    composable<AccountNavigationDestination.EditAccount> {
        val accountComponentBuilder = LocalFeatureAccountComponentBuilder.current
        val accountComponent = remember { accountComponentBuilder.build() }
        AccountEditRoute(
            viewModel = viewModel(factory = accountComponent.viewModelFactory()),
            popBack = { navController.popBackStack() }
        )
    }
    composable<T> {
        val accountComponentBuilder = LocalFeatureAccountComponentBuilder.current
        val accountComponent = remember { accountComponentBuilder.build() }
        AccountRoute(
            viewModel = viewModel(factory = accountComponent.viewModelFactory()),
            onClickEdit = { navController.navigate(AccountNavigationDestination.EditAccount) }
        )
    }
}