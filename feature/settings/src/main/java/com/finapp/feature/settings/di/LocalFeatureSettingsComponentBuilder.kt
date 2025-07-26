package com.finapp.feature.settings.di

import androidx.compose.runtime.staticCompositionLocalOf

val LocalFeatureSettingsComponentBuilder = staticCompositionLocalOf<FeatureSettingsComponent.Builder> {
    error("FeatureSettingsComponent not provided")
}