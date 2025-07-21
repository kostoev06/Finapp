plugins {
    id(libs.plugins.android.library.get().pluginId)
    alias(libs.plugins.ksp)
    `finapp-module-android`
}

android {
    namespace = "com.finapp.core.work"
}

dependencies {
    implementation(libs.dagger)
    api(project(":core:data:api"))
    ksp(libs.dagger.compiler)
    implementation(libs.androidx.datastore.core)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.work.runtime.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
}