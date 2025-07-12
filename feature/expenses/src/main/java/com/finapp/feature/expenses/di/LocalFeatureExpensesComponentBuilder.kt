package com.finapp.feature.expenses.di

import androidx.compose.runtime.staticCompositionLocalOf

val LocalFeatureExpensesComponentBuilder = staticCompositionLocalOf<FeatureExpensesComponent.Builder> {
    error("FeatureExpensesComponent not provided")
}