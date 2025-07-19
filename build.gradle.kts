// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id(libs.plugins.android.application.get().pluginId) apply false
    id(libs.plugins.kotlin.android.get().pluginId) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    id(libs.plugins.jetbrains.kotlin.jvm.get().pluginId) apply false
}