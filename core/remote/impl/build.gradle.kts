import java.io.FileInputStream
import java.util.Properties

plugins {
    id(libs.plugins.android.library.get().pluginId)
    `finapp-module-android`
    id(libs.plugins.ksp.get().pluginId)
}

android {
    namespace = "com.finapp.core.remote.impl"
    defaultConfig {
        val localProperties = Properties()
        val localFile = rootProject.file("local.properties")
        if (localFile.exists()) {
            localProperties.load(FileInputStream(localFile))
        }
        val apiKey = localProperties.getProperty("API_KEY")
        buildConfigField("String", "API_KEY", "\"${apiKey}\"")
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(project(":core:remote:api"))

    api(libs.retrofit.core)
    api(libs.retrofit.kotlin.serialization)
    implementation(libs.converter.gson)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}