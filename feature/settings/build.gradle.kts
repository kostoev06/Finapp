plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    alias(libs.plugins.kotlin.serialization)
    `finapp-module-feature`
}

android {
    namespace = "com.finapp.feature.settings"
}

dependencies {
    implementation(project(":feature:common"))
    implementation(project(":core:settings:impl"))
    implementation(project(":core:data:api"))
}