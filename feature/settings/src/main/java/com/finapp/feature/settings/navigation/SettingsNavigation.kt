package com.finapp.feature.settings.navigation

import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.finapp.feature.settings.BrandColorPickerRoute
import com.finapp.feature.settings.LanguagePickerRoute
import com.finapp.feature.settings.SettingsRoute
import com.finapp.feature.settings.about.AboutRoute
import com.finapp.feature.settings.di.LocalFeatureSettingsComponentBuilder
import com.finapp.feature.settings.haptics.HapticsRoute
import com.finapp.feature.settings.passcode.PasscodeRoute
import com.finapp.feature.settings.sound.SoundRoute
import com.finapp.feature.settings.sync.SyncRoute

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
    composable<SettingsNavigationDestination.Passcode> {
        val settingsComponentBuilder = LocalFeatureSettingsComponentBuilder.current
        val settingsComponent = remember { settingsComponentBuilder.build() }
        PasscodeRoute(
            viewModel = viewModel(factory = settingsComponent.viewModelFactory()),
            onBack = { navController.popBackStack() },
            onDone = { navController.popBackStack() }
        )
    }
    composable<SettingsNavigationDestination.LanguagePicker> {
        val settingsComponentBuilder = LocalFeatureSettingsComponentBuilder.current
        val settingsComponent = remember { settingsComponentBuilder.build() }
        LanguagePickerRoute(
            viewModel = viewModel(factory = settingsComponent.viewModelFactory()),
            onBack = { navController.popBackStack() }
        )
    }
    composable<SettingsNavigationDestination.Sync> {
        val settingsComponentBuilder = LocalFeatureSettingsComponentBuilder.current
        val settingsComponent = remember { settingsComponentBuilder.build() }
        SyncRoute(
            viewModel = viewModel(factory = settingsComponent.viewModelFactory()),
            onBack = { navController.popBackStack() }
        )
    }
    composable<SettingsNavigationDestination.Sound> {
        val settingsComponentBuilder = LocalFeatureSettingsComponentBuilder.current
        val settingsComponent = remember { settingsComponentBuilder.build() }
        SoundRoute(
            viewModel = viewModel(factory = settingsComponent.viewModelFactory()),
            onBack = { navController.popBackStack() }
        )
    }
    composable<SettingsNavigationDestination.Haptics> {
        val settingsComponentBuilder = LocalFeatureSettingsComponentBuilder.current
        val settingsComponent = remember { settingsComponentBuilder.build() }
        HapticsRoute(
            viewModel = viewModel(factory = settingsComponent.viewModelFactory()),
            onBack = { navController.popBackStack() }
        )
    }
    composable<T> {
        val settingsComponentBuilder = LocalFeatureSettingsComponentBuilder.current
        val settingsComponent = remember { settingsComponentBuilder.build() }
        SettingsRoute(
            viewModel = viewModel(factory = settingsComponent.viewModelFactory()),
            onOpenColorPicker = { navController.navigate(SettingsNavigationDestination.BrandColorPicker) },
            onOpenAbout = { navController.navigate(SettingsNavigationDestination.About) },
            onOpenPasscode = { mode -> navController.navigate(SettingsNavigationDestination.Passcode(mode)) },
            onOpenLanguage = { navController.navigate(SettingsNavigationDestination.LanguagePicker) },
            onOpenSync = { navController.navigate(SettingsNavigationDestination.Sync) },
            onOpenSound = { navController.navigate(SettingsNavigationDestination.Sound) },
            onOpenHaptics = { navController.navigate(SettingsNavigationDestination.Haptics) }
        )
    }
}
