package com.finapp.feature.income.di

import androidx.compose.runtime.staticCompositionLocalOf

val LocalFeatureIncomeComponentBuilder = staticCompositionLocalOf<FeatureIncomeComponent.Builder> {
    error("FeatureIncomeComponent not provided")
}