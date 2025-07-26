package com.finapp.finapp.theme.di

import androidx.compose.runtime.staticCompositionLocalOf

val LocalFeatureMainComponentBuilder = staticCompositionLocalOf<FeatureMainComponent.Builder> {
        error("FeatureMainComponent not provided")
    }