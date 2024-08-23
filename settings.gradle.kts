pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven {
            url = uri("https://packages.aliyun.com/maven/repository/2418478-release-GhmPUt/")
            credentials {
                username = "609399173a10edbf367f5264"
                password = "=RTs0bvMruGT"
            }
        }
        mavenCentral()
        google()
        maven(url = "https://jitpack.io")
    }
    versionCatalogs {
        create("libs") {
            library("base", "io.github.chawloo", "QuickBaseLib").version("1.6.1")
        }
    }
}
rootProject.name = "QuickBaseLib"
include(":app")
include(":QuickBaseLib")
