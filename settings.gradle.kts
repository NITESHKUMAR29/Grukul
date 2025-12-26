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

rootProject.name = "Gurukul"
include(":app")



include(":core:core-ui")
include(":core:core-network")
include(":core:core-database")
include(":core:core-model")
include(":core:core-utils")
include(":core:core-common")
include(":feature:feature-auth")
include(":core:core-firebase")
include(":feature:feature-home")
