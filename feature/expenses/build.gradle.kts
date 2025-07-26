plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    alias(libs.plugins.kotlin.serialization)
    `finapp-module-feature`
}

android {
    namespace = "com.finapp.feature.expenses"
}

dependencies {
    implementation(project(":feature:common"))
    implementation(project(":feature:charts"))
    implementation(project(":core:data:api"))
}