pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven(url = "https://maven.aliyun.com/repository/central")
        maven(url = "https://maven.aliyun.com/repository/public")
        maven(url = "https://maven.aliyun.com/repository/google")
        maven(url = "https://maven.aliyun.com/repository/gradle-plugin")
        maven(url = "https://jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://maven.aliyun.com/repository/central")
        maven(url = "https://maven.aliyun.com/repository/public")
        maven(url = "https://maven.aliyun.com/repository/google")
        maven(url = "https://maven.aliyun.com/repository/gradle-plugin")
        maven(url = "https://jitpack.io")
    }
    versionCatalogs {
        create("libs") {
            version("minSdk", "26")
            version("targetSdk", "33")
            version("compileSdk", "33")
            version("kotlin", "1.8.10")
            plugin("kotlin-android", "org.jetbrains.kotlin.android").versionRef("kotlin")
            plugin("kotlin-kapt", "org.jetbrains.kotlin.kapt").versionRef("kotlin")

            library("stdlib-jdk7", "org.jetbrains.kotlin", "kotlin-stdlib-jdk7").versionRef("kotlin")
            library("stdlib-jdk8", "org.jetbrains.kotlin", "kotlin-stdlib-jdk8").versionRef("kotlin")
            library("reflect", "org.jetbrains.kotlin", "kotlin-reflect").versionRef("kotlin")
            bundle("kotlin", listOf("stdlib-jdk7", "stdlib-jdk8", "reflect"))

            version("coroutines", "1.6.4")
            library("kotlinx-coroutines-core", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").versionRef("coroutines")
            library("kotlinx-coroutines-android", "org.jetbrains.kotlinx", "kotlinx-coroutines-android").versionRef("coroutines")
            bundle("coroutines", listOf("kotlinx-coroutines-core", "kotlinx-coroutines-android"))

            library("kotlinx-serialization-json", "org.jetbrains.kotlinx", "kotlinx-serialization-json").version("1.5.0-RC")

            library("core-ktx", "androidx.core", "core-ktx").version("1.9.0")
            library("appcompat", "androidx.appcompat", "appcompat").version("1.6.0")
            library("multidex", "androidx.multidex", "multidex").version("2.0.1")
            library("activity-ktx", "androidx.activity", "activity-ktx").version("1.6.1")
            library("fragment-ktx", "androidx.fragment", "fragment-ktx").version("1.5.5")
            library("annotation", "androidx.annotation", "annotation").version("1.5.0")
            library("constraintlayout", "androidx.constraintlayout", "constraintlayout").version("2.1.4")
            library("recyclerview", "androidx.recyclerview", "recyclerview").version("1.2.1")
            library("startup-runtime", "androidx.startup", "startup-runtime").version("1.1.1")

            version("lifecycle", "2.5.1")
            library("viewmodel", "androidx.lifecycle", "lifecycle-viewmodel-ktx").versionRef("lifecycle")
            library("livedata", "androidx.lifecycle", "lifecycle-livedata-ktx").versionRef("lifecycle")

            version("room", "2.5.0")
            library("room-runtime", "androidx.room", "room-runtime").versionRef("room")
            library("room-ktx", "androidx.room", "room-ktx").versionRef("room")
            library("room-compiler", "androidx.room", "room-compiler").versionRef("room")
            bundle("room", listOf("room-runtime", "room-ktx"))

            version("koin", "3.3.3")
            library("koin-android", "io.insert-koin", "koin-android").versionRef("koin")

            library("androidautosize", "me.jessyan", "autosize").version("1.2.1")
            library("material", "com.google.android.material", "material").version("1.5.0-alpha04")
            library("retrofit", "com.squareup.retrofit2", "retrofit").version("2.9.0")
            library("okhttp", "com.squareup.okhttp3", "okhttp").version("5.0.0-alpha.10")
            library("brv", "com.github.liangjingkanji", "BRV").version("1.3.90")
            library("jodatime", "joda-time", "joda-time").version("2.12.2")
            library("basePopup", "io.github.razerdp", "BasePopup").version("3.2.0")
            library("mmkv", "com.tencent", "mmkv").version("1.2.15")
            library("xPermission", "com.github.getActivity", "XXPermissions").version("16.6")
            library("wheelView", "com.github.zyyoona7", "wheelview").version("2.0.4")
            library("toast", "com.github.getActivity", "ToastUtils").version("11.2")
            library("viewbinding-ktx", "com.github.DylanCaiCoding.ViewBindingKTX", "viewbinding-ktx").version("2.1.0")
            library("banner", "io.github.youth5201314", "banner").version("2.2.2")
            library("flexbox", "com.google.android.flexbox", "flexbox").version("3.0.0")
            library("evenbus", "org.greenrobot", "eventbus").version("3.3.1")

            version("arouter", "1.5.2")
            library("arouter-compiler", "com.alibaba", "arouter-compiler").versionRef("arouter")
            library("arouter-api", "com.alibaba", "arouter-api").versionRef("arouter")

            library("immersionbar", "com.geyifeng.immersionbar", "immersionbar").version("3.2.2")
            library("immersionbar-ktx", "com.geyifeng.immersionbar", "immersionbar-ktx").version("3.2.2")
            bundle("immersionbar", listOf("immersionbar", "immersionbar-ktx"))

            version("saf-log", "2.6.8")
            library("saf-log-core", "com.github.fengzhizi715.SAF-Kotlin-log", "core").versionRef("saf-log")
            library("saf-log-okhttp", "com.github.fengzhizi715", "saf-logginginterceptor").version("v1.6.13")
            bundle("saf-log", listOf("saf-log-core", "saf-log-okhttp"))

            library("x5webview", "com.tencent.tbs", "tbssdk").version("44275")
            library("wechat-sdk-android-without-mta", "com.tencent.mm.opensdk", "wechat-sdk-android-without-mta").version("6.8.0")

            version("coil", "2.2.2")
            library("coil", "io.coil-kt", "coil").versionRef("coil")
            library("coil-gif", "io.coil-kt", "coil-gif").versionRef("coil")
            bundle("coil", listOf("coil", "coil-gif"))

            library("junit", "junit", "junit").version("4.13.2")
        }
    }
}
rootProject.name = "QuickBaseLib"
include(":app")
include(":QuickBaseLib")
