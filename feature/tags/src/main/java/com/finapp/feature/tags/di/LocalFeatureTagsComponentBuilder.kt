package com.finapp.feature.tags.di

import androidx.compose.runtime.staticCompositionLocalOf

val LocalFeatureTagsComponentBuilder = staticCompositionLocalOf<FeatureTagsComponent.Builder> {
    error("FeatureTagsComponent not provided")
}