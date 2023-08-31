plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp")
    id("therouter")
}
android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "cn.chawloo.basedemo"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    dataBinding {
        enable = true
    }
    namespace = "cn.chawloo.basedemo"
}

dependencies {
    implementation(project(":QuickBaseLib"))
    testImplementation(libs.junit)
    api(libs.therouter)
    ksp(libs.therouter.apt)
}