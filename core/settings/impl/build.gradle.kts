plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    alias(libs.plugins.ksp)
    `finapp-module-android`
}

android {
    namespace = "com.finapp.core.settings.impl"
}

dependencies {
    api(project(":core:settings:api"))

    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.dagger)
    api(libs.androidx.datastore.core)
    api(libs.androidx.datastore.preferences)
    implementation(libs.material)
    ksp(libs.dagger.compiler)
}