plugins{
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}
kapt {
    arguments {
        arg("AROUTER_MODULE_NAME", "update")
    }
}
android{
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig{
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    namespace = "cn.chawloo.base"
}
dependencies {
    api(libs.bundles.kotlin)
    api(libs.bundles.coroutines)
    api(libs.kotlinx.serialization.json)

    api(libs.core.ktx)
    api(libs.appcompat)
    api(libs.multidex)
    api(libs.startup.runtime)
    api(libs.viewmodel)
    api(libs.livedata)
    api(libs.activity.ktx)
    api(libs.fragment.ktx)
    api(libs.bundles.room)

    api(libs.material)
    api(libs.brv)
    api(libs.jodatime)
    api(libs.arouter.api)
    kapt(libs.arouter.compiler)
    api(libs.androidautosize)
    api(libs.basePopup)
    api(libs.toast)
    api(libs.koin.android)
    api(libs.koin.annotations)
    api(libs.okhttp)
    api(libs.retrofit)
    api(libs.permissionx)
    api(libs.mmkv)
    api(libs.multistatepage)
    api(libs.viewbinding.ktx)
    api(libs.jodatime)
    api(libs.wechat.sdk.android.without.mta)

    api(libs.bundles.coil)
    api(libs.bundles.saf.log)
    api(libs.bundles.immersionbar)

    testImplementation(libs.junit)
}
