// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application").version("8.0.2").apply(false)
    id("com.android.library").version("8.0.2").apply(false)
    kotlin("android").version("1.8.21").apply(false)
    id("com.google.devtools.ksp").version("1.8.21-1.0.11").apply(false)
    id("cn.therouter").version("1.2.0-beta2").apply(false)
}

task<Delete>("clean") {
    group = "build"
    delete(rootProject.buildDir)
}