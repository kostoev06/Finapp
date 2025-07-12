package com.finapp.feature.account.di

import androidx.compose.runtime.staticCompositionLocalOf

val LocalFeatureAccountComponentBuilder = staticCompositionLocalOf<FeatureAccountComponent.Builder> {
    error("FeatureAccountComponent not provided")
}