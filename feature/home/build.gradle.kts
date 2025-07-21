plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    alias(libs.plugins.kotlin.serialization)
    `finapp-module-feature`
}

android {
    namespace = "com.finapp.feature.home"
}

dependencies {
    implementation(project(":feature:common"))
    implementation(project(":feature:expenses"))
    implementation(project(":feature:income"))
    implementation(project(":feature:account"))
    implementation(project(":feature:tags"))
    implementation(project(":feature:settings"))
    implementation(project(":core:data:api"))
    implementation(project(":core:work"))
}