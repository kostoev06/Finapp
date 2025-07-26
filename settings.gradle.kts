pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Finapp"
include(":app")
include(":core:common")
include(":core:remote:api")
include(":core:remote:impl")
include(":core:data:api")
include(":core:data:impl")
include(":feature:common")
include(":feature:home")
include(":feature:expenses")
include(":feature:income")
include(":feature:account")
include(":feature:tags")
include(":feature:settings")
include(":core:database:api")
include(":core:database:impl")
include(":core:work")
include(":feature:charts")
include(":core:settings:api")
include(":core:settings:impl")
