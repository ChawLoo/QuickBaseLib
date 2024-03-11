// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    kotlin("android") version libs.versions.kotlin.get() apply false
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.therouter) apply false
}
val ver: String get() = "1.5.4".also { println("当前插件版本为：${it}") }
extra["version"] = ver

task<Delete>("clean") {
    group = "build"
    delete(rootProject.buildDir)
}