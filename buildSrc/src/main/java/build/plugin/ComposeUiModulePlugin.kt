package build.plugin

import build.extensions.androidTestImplementation
import build.extensions.debugImplementation
import build.extensions.implementation
import build.extensions.libs
import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class ComposeUiModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(libs.plugins.ksp.get().pluginId)
            pluginManager.apply(libs.plugins.kotlin.compose.get().pluginId)

            pluginManager.apply("finapp-module-android")

            project.extensions.getByType<BaseExtension>().apply {
                defaultConfig {
                    vectorDrawables {
                        useSupportLibrary = true
                    }
                }

                buildFeatures.compose = true

                composeOptions {
                    kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get().toString()
                }

                packagingOptions {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }
            }

            dependencies {
                val composeBom = platform(libs.androidx.compose.bom)

                implementation(composeBom)
                implementation(libs.androidx.compose.foundation)
                implementation(libs.androidx.compose.runtime)
                implementation(libs.androidx.compose.material.icons.core)
                implementation(libs.androidx.compose.material3)
                implementation(libs.compose.material)
                implementation(libs.androidx.compose.ui.tooling.preview)
                implementation(libs.androidx.ui)
                implementation(libs.androidx.ui.graphics)
                implementation(libs.kotlinx.collections.immutable)
                implementation(libs.kotlinx.coroutines.android)
                implementation(libs.androidx.core.ktx)
                implementation(libs.androidx.activity.compose)
            }
        }
    }
}