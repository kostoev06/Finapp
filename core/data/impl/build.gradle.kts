import java.io.FileInputStream
import java.util.Properties

plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    alias(libs.plugins.ksp)
    `finapp-module-android`
}

android {
    namespace = "com.finapp.core.data.impl"

    defaultConfig {
        val localProperties = Properties()
        val localFile = rootProject.file("local.properties")
        if (localFile.exists()) {
            localProperties.load(FileInputStream(localFile))
        }
        val accountId = localProperties.getProperty("ACCOUNT_ID")
        buildConfigField("long", "ACCOUNT_ID", accountId)
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(project(":core:data:api"))
    implementation(project(":core:remote:impl"))
    implementation(project(":core:database:impl"))

    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.dagger)
    implementation(libs.androidx.datastore.core)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.material)
    ksp(libs.dagger.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}