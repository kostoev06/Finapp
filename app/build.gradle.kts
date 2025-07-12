import java.io.FileInputStream
import java.util.Properties

plugins {
    id(libs.plugins.android.application.get().pluginId)
    `finapp-module-feature`
}

android {
    namespace = "com.finapp.finapp"
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
    implementation(project(":core:remote:impl"))
    implementation(libs.androidx.core.splashscreen)
}
