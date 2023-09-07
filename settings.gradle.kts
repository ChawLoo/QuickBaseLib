pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        val localUserName = file("maven.properties").takeIf { it.canRead() }?.run {
            val versionProps = java.util.Properties()
            versionProps.load(inputStream())
            versionProps["maven.local.username"].toString()
        }
        val localPwd = file("maven.properties").takeIf { it.canRead() }?.run {
            val versionProps = java.util.Properties()
            versionProps.load(inputStream())
            versionProps["maven.local.password"].toString()
        }
        maven {
            isAllowInsecureProtocol = true
            url = uri("http://47.97.187.94:8536/repository/maven-android/")
            credentials {
                username = localUserName
                password = localPwd
            }
        }
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
    versionCatalogs {
        create("libs") {
            from("io.github.chawloo:VersionControlPlugin:1.3.0")
        }
    }
}
rootProject.name = "QuickBaseLib"
include(":app")
include(":QuickBaseLib")
