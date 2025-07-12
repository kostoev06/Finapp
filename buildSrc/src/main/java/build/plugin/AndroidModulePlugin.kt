package build.plugin

import Config
import build.extensions.implementation
import build.extensions.testImplementation
import build.extensions.androidTestImplementation
import build.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile


class AndroidModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugins.kotlin.android.get().pluginId)
            }

            project.extensions.getByType<BaseExtension>().apply {
                compileSdkVersion(Config.compileSdk)

                defaultConfig {
                    minSdk = Config.minSdk
                    targetSdk = Config.targetSdk

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }

                project.tasks.withType(KotlinJvmCompile::class.java).configureEach {
                    compilerOptions {
                        jvmTarget.set(JvmTarget.fromTarget("11"))
                    }
                }

                when (this) {
                    is LibraryExtension -> {
                        defaultConfig {
                            consumerProguardFiles("consumer-rules.pro")
                        }
                    }

                    is AppExtension -> {
                        defaultConfig {
                            versionCode = Config.versionCode
                            versionName = Config.versionName
                        }

                        buildTypes {
                            getByName("release") {
                                isMinifyEnabled = Config.isMinifyEnabled
                                isShrinkResources = Config.isShrinkResources
                                proguardFiles(
                                    getDefaultProguardFile("proguard-android-optimize.txt"),
                                    "proguard-rules.pro"
                                )
                            }
                        }
                    }
                }
            }

            dependencies {
            }
        }
    }
}