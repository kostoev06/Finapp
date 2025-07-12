plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    `finapp-module-feature`
}

android {
    namespace = "com.finapp.feature.tags"
}

dependencies {
    implementation(project(":feature:common"))
    implementation(project(":core:data:api"))
}