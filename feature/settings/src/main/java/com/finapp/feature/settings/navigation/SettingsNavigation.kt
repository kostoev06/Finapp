package com.finapp.feature.settings.navigation

import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.finapp.feature.settings.BrandColorPickerRoute
import com.finapp.feature.settings.SettingsRoute
import com.finapp.feature.settings.about.AboutRoute
import com.finapp.feature.settings.di.LocalFeatureSettingsComponentBuilder

inline fun <reified T : Any> NavGraphBuilder.settingsNavigation(
    navController: NavController
) {
    composable<SettingsNavigationDestination.BrandColorPicker> {
        val settingsComponentBuilder = LocalFeatureSettingsComponentBuilder.current
        val settingsComponent = remember { settingsComponentBuilder.build() }
        BrandColorPickerRoute(
            viewModel = viewModel(factory = settingsComponent.viewModelFactory()),
            onBack = { navController.popBackStack() },
        )
    }
    composable<SettingsNavigationDestination.About> {
        val settingsComponentBuilder = LocalFeatureSettingsComponentBuilder.current
        val settingsComponent = remember { settingsComponentBuilder.build() }
        AboutRoute(
            viewModel = viewModel(factory = settingsComponent.viewModelFactory()),
            onBack = { navController.popBackStack() },
        )
    }
    composable<T> {
        val settingsComponentBuilder = LocalFeatureSettingsComponentBuilder.current
        val settingsComponent = remember { settingsComponentBuilder.build() }
        SettingsRoute(
            viewModel = viewModel(factory = settingsComponent.viewModelFactory()),
            onOpenColorPicker = { navController.navigate(SettingsNavigationDestination.BrandColorPicker) },
            onOpenAbout = { navController.navigate(SettingsNavigationDestination.About) }
        )
    }
}