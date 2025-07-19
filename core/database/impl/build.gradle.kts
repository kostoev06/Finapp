plugins {
    id(libs.plugins.android.library.get().pluginId)
    `finapp-module-android`
    id(libs.plugins.ksp.get().pluginId)
}

android {
    namespace = "com.finapp.core.database.impl"
}

dependencies {
    api(project(":core:database:api"))
    implementation(project(":core:common"))

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.dagger)
    ksp(libs.dagger.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}