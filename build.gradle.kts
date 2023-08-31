// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    kotlin("android") version libs.versions.kotlin.get() apply false
    kotlin("kapt") version libs.versions.kotlin.get() apply false
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.therouter) apply false
}

task<Delete>("clean") {
    group = "build"
    delete(rootProject.buildDir)
}