plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(gradleApi())
    implementation(files((libs).javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("appAndroidModulePlugin") {
            id = libs.plugins.finapp.module.android.get().pluginId
            implementationClass = "build.plugin.AndroidModulePlugin"
        }
//        register("appRemoteModule") {
//            id = libs.plugins.notool.module.remote.get().pluginId
//            implementationClass = "build.plugin.RemoteModulePlugin"
//        }
        register("appComposeUiModule") {
            id = libs.plugins.finapp.module.ui.compose.get().pluginId
            implementationClass = "build.plugin.ComposeUiModulePlugin"
        }
        register("appFeatureModule") {
            id = libs.plugins.finapp.module.feature.get().pluginId
            implementationClass = "build.plugin.FeatureModulePlugin"
        }
    }
}
