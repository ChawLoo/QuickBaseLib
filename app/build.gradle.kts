plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp")
    id("therouter")
}
android {
    signingConfigs {
        create("release") {
            storeFile = file("./QuickBaseLib")
            storePassword = "ChawLoo0827"
            keyAlias = "QuickBaseLib"
            keyPassword = "ChawLoo0827"
            enableV1Signing = true
            enableV2Signing = true
        }
    }
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
            isMinifyEnabled = true
            isDebuggable = true
            signingConfig = signingConfigs.getByName("release")
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
    buildFeatures {
        viewBinding = true
    }
    namespace = "cn.chawloo.basedemo"
}

dependencies {

    implementation(libs.base)
//    implementation(project(":QuickBaseLib"))
    testImplementation(libs.junit)
    api(libs.therouter)
    ksp(libs.therouter.apt)
}