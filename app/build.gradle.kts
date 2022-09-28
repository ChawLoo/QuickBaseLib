plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}
kapt {
    arguments {
        arg("AROUTER_MODULE_NAME", "update")
    }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":QuickBaseLib"))
    testImplementation(libs.junit)
}