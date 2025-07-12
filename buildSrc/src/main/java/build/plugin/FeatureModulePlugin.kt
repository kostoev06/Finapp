package build.plugin

import build.extensions.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class FeatureModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("finapp-module-ui-compose")
            pluginManager.apply(libs.plugins.ksp.get().pluginId)
            pluginManager.apply(libs.plugins.kotlin.serialization.get().pluginId)

            dependencies {
                implementation(project(":core:common"))
                implementation(libs.navigation.compose)
                implementation(libs.dagger)
                implementation(libs.androidx.lifecycle.runtime.ktx)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.androidx.lifecycle.viewmodel.compose)
                ksp(libs.dagger.compiler)
            }
        }
    }
}