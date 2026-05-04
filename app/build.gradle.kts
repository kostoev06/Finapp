plugins {
    id(libs.plugins.android.application.get().pluginId)
    `finapp-module-feature`
}

android {
    namespace = "com.finapp.finapp"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":feature:common"))
    implementation(project(":feature:home"))
    implementation(project(":feature:expenses"))
    implementation(project(":feature:income"))
    implementation(project(":feature:account"))
    implementation(project(":feature:tags"))
    implementation(project(":feature:settings"))
    implementation(project(":core:data:impl"))
    implementation(project(":core:domain"))
    implementation(project(":core:remote:impl"))
    implementation(project(":core:database:impl"))
    implementation(project(":core:settings:impl"))
    implementation(project(":core:work"))
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.work.runtime.ktx)
    implementation(libs.androidx.appcompat)
}
