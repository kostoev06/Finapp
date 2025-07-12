package com.finapp.feature.home.di

import androidx.compose.runtime.staticCompositionLocalOf


val LocalFeatureHomeComponentBuilder = staticCompositionLocalOf<FeatureHomeComponent.Builder> {
    error("FeatureHomeComponent not provided")
}