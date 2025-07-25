package com.finapp.feature.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.finapp.feature.common.theme.GreenPrimary
import com.finapp.feature.common.theme.GreenPrimaryLight
import com.finapp.feature.home.di.LocalFeatureHomeComponentBuilder
import com.finapp.feature.home.navigation.HomeNavigationDestination
import com.finapp.feature.home.navigation.HomeNavigationGraph

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = viewModel(factory = LocalFeatureHomeComponentBuilder.current.build().viewModelFactory()),
    modifier: Modifier = Modifier
) {
    val destinations =
        HomeNavigationDestination.destinations.map { it::class.java.kotlin.qualifiedName }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentDestination in destinations) {
                HomeBottomNavBar(navController)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        HomeNavigationGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun HomeBottomNavBar(
    navController: NavHostController
) {
    val destinations = HomeNavigationDestination.destinations

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        destinations.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = screen.iconId),
                        contentDescription = stringResource(screen.titleId)
                    )
                },
                label = {
                    Text(
                        text = stringResource(screen.titleId),
                        fontWeight = FontWeight.W500
                    )
                },
                selected = currentDestination?.hierarchy?.any { it.route == screen::class.qualifiedName } == true,
                onClick = {
                    navController.navigate(screen) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}