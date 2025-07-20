import java.io.FileInputStream
import java.util.Properties

plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    alias(libs.plugins.kotlin.serialization)
    `finapp-module-feature`
}

android {
    namespace = "com.finapp.feature.account"

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
    implementation(project(":feature:common"))
    implementation(project(":core:data:api"))
}