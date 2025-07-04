package com.example.finapp.ui.feature.account.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.finapp.ui.feature.account.AccountRoute
import com.example.finapp.ui.feature.account.edit.EditAccountRoute
import com.example.finapp.ui.feature.home.navigation.HomeNavigationDestination


inline fun <reified T : HomeNavigationDestination> NavGraphBuilder.accountNavigation(
    navController: NavController
) {
    composable<AccountNavigationDestination.EditAccount> {
        EditAccountRoute(popBack = { navController.popBackStack() })
    }
    composable<T> {
        AccountRoute(onClickEdit = { navController.navigate(AccountNavigationDestination.EditAccount) })
    }
}