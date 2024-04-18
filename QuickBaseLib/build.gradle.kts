plugins {
    id("com.android.library")
    kotlin("android")
    `version-catalog`
    `maven-publish`
    signing
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
    namespace = "cn.chawloo.base"
}

dependencies {
    implementation(libs.material)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.brv)
    implementation(libs.therouter)
    implementation(libs.androidautosize)
    implementation(libs.basePopup)
    implementation(libs.toast)
    implementation(libs.net)
    implementation(libs.mmkv)
    implementation(libs.viewbinding.ktx)


    implementation(libs.bundles.androidx)
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.saf.log)
    implementation(libs.bundles.immersionbar)

    testImplementation(libs.junit)
}

catalog {
    versionCatalog {
        version("minSdk", "26")
        version("targetSdk", "34")
        version("compileSdk", "34")
        version("buildToolsVersion", "34.0.0")
        version("compileSdkPreview", "UpsideDownCake")
        version("kotlin", "1.9.23")
        version("gradleVer", "8.3.2")

        plugin("android-application", "com.android.application").versionRef("gradleVer")
        plugin("android-library", "com.android.library").versionRef("gradleVer")
        plugin("kotlin-android", "org.jetbrains.kotlin.android").versionRef("kotlin")
        plugin("kotlin-kapt", "org.jetbrains.kotlin.kapt").versionRef("kotlin")
        plugin("kotlin-parcelize", "org.jetbrains.kotlin.plugin.parcelize").versionRef("kotlin")
        plugin("kotlin-serialization", "org.jetbrains.kotlin.plugin.serialization").versionRef("kotlin")

        version("ksp", "1.9.23-1.0.20")
        plugin("ksp", "com.google.devtools.ksp").versionRef("ksp")


        library("stdlib-jdk7", "org.jetbrains.kotlin", "kotlin-stdlib-jdk7").versionRef("kotlin")
        library("stdlib-jdk8", "org.jetbrains.kotlin", "kotlin-stdlib-jdk8").versionRef("kotlin")
        library("reflect", "org.jetbrains.kotlin", "kotlin-reflect").versionRef("kotlin")
        bundle(
            "kotlin", listOf(
                "stdlib-jdk7",
                "stdlib-jdk8",
                "reflect"
            )
        )

        version("coroutines", "1.8.0")
        library("kotlinx-coroutines-core", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").versionRef("coroutines")
        library("kotlinx-coroutines-android", "org.jetbrains.kotlinx", "kotlinx-coroutines-android").versionRef("coroutines")
        bundle("coroutines", listOf("kotlinx-coroutines-core", "kotlinx-coroutines-android"))



        version("activity", "1.8.2")
        library("activity-ktx", "androidx.activity", "activity-ktx").versionRef("activity")
        version("core-ktx", "1.12.0")
        library("core-ktx", "androidx.core", "core-ktx").versionRef("core-ktx")
        version("appcompat", "1.6.1")
        library("appcompat", "androidx.appcompat", "appcompat").versionRef("appcompat")
        version("multidex", "2.0.1")
        library("multidex", "androidx.multidex", "multidex").versionRef("multidex")
        version("fragment", "1.6.2")
        library("fragment-ktx", "androidx.fragment", "fragment-ktx").versionRef("fragment")
        version("annotation", "1.7.1")
        library("annotation", "androidx.annotation", "annotation").versionRef("annotation")
        version("constraintlayout", "2.1.4")
        library("constraintlayout", "androidx.constraintlayout", "constraintlayout").versionRef("constraintlayout")
        version("recyclerview", "1.3.2")
        library("recyclerview", "androidx.recyclerview", "recyclerview").versionRef("recyclerview")
        version("splashscreen","1.0.1")
        library("splashscreen", "androidx.core", "core-splashscreen").versionRef("splashscreen")

        bundle(
            "androidx", listOf(
                "core-ktx",
                "appcompat",
                "multidex",
                "activity-ktx",
                "fragment-ktx",
                "constraintlayout",
                "recyclerview",
                "splashscreen",
            )
        )



        version("room", "2.6.1")
        library("room-runtime", "androidx.room", "room-runtime").versionRef("room")
        library("room-ktx", "androidx.room", "room-ktx").versionRef("room")
        library("room-compiler", "androidx.room", "room-compiler").versionRef("room")
        bundle("room", listOf("room-runtime", "room-ktx"))

        library("androidautosize", "com.github.JessYanCoding", "AndroidAutoSize").version("v1.2.1")
        library("material", "com.google.android.material", "material").version("1.11.0")
//        library("retrofit", "com.squareup.retrofit2", "retrofit").version("2.11.0")
        library("okhttp", "com.squareup.okhttp3", "okhttp").version("4.12.0")
        library("net", "com.github.liangjingkanji", "Net").version("3.6.4")
        library("brv", "com.github.liangjingkanji", "BRV").version("1.5.8")

        library("kotlinx-serialization-json", "org.jetbrains.kotlinx", "kotlinx-serialization-json").version("1.6.3")
        library("crashReport", "com.tencent.bugly", "crashreport").version("4.1.9.3")
        library("basePopup", "io.github.razerdp", "BasePopup").version("3.2.1")
        library("mmkv", "com.tencent", "mmkv").version("1.3.4")
        library("xPermission", "com.github.getActivity", "XXPermissions").version("18.62")
        library("wheelView", "com.github.zyyoona7.WheelPicker", "wheelview").version("2.0.7")
        library("toast", "com.github.getActivity", "Toaster").version("12.6")
        library("viewbinding-ktx", "com.github.DylanCaiCoding.ViewBindingKTX", "viewbinding-ktx").version("2.1.0")
        library("banner", "io.github.youth5201314", "banner").version("2.2.3")
        library("flexbox", "com.google.android.flexbox", "flexbox").version("3.0.0")

        version("therouter", "1.2.2")
        library("therouter", "cn.therouter", "router").versionRef("therouter")
        library("therouter-apt", "cn.therouter", "apt").versionRef("therouter")
        plugin("therouter", "cn.therouter.agp8").versionRef("therouter")

        version("immersionbar", "3.2.2")
        library("immersionbar", "com.geyifeng.immersionbar", "immersionbar").versionRef("immersionbar")
        library("immersionbar-ktx", "com.geyifeng.immersionbar", "immersionbar-ktx").versionRef("immersionbar")
        bundle("immersionbar", listOf("immersionbar", "immersionbar-ktx"))

        version("pictureSelector", "v3.11.2")
        library("pictureSelector", "io.github.lucksiege", "pictureselector").versionRef("pictureSelector")
        library("pictureSelector-compress", "io.github.lucksiege", "compress").versionRef("pictureSelector")
        library("pictureSelector-ucrop", "io.github.lucksiege", "ucrop").versionRef("pictureSelector")
        library("pictureSelector-camerax", "io.github.lucksiege", "camerax").versionRef("pictureSelector")
        bundle(
            "pictureSelector", listOf(
                "pictureSelector",
                "pictureSelector-compress",
                "pictureSelector-ucrop",
                "pictureSelector-camerax"
            )
        )

        version("saf-log", "2.6.9")
        library("saf-log-core", "com.github.fengzhizi715.SAF-Kotlin-log", "core").versionRef("saf-log")
        library("saf-log-okhttp", "com.github.fengzhizi715", "saf-logginginterceptor").version("v1.6.13")
        bundle("saf-log", listOf("saf-log-core", "saf-log-okhttp"))


        library("x5webview", "com.tencent.tbs", "tbssdk").version("44286")
        library("wechat-sdk-android-without-mta", "com.tencent.mm.opensdk", "wechat-sdk-android-without-mta").version("6.8.0")

        version("coil", "2.6.0")
        library("coil", "io.coil-kt", "coil").versionRef("coil")
        library("coil-gif", "io.coil-kt", "coil-gif").versionRef("coil")
        library("compose-coil", "io.coil-kt", "coil-compose").versionRef("coil")
        bundle("coil", listOf("coil", "coil-gif"))

        library("androidx-compose-bom", "androidx.compose", "compose-bom").version("2024.04.00")
        // Material Design 3
        library("compose-material3", "androidx.compose.material3", "material3").withoutVersion()
        // or Material Design 2
        library("compose-material", "androidx.compose.material", "material").withoutVersion()
        // or only import the main APIs for the underlying toolkit systems,
        // such as input and measurement/layout
        library("compose-ui", "androidx.compose.ui", "ui").withoutVersion()
        library("compose-ui-tooling-preview", "androidx.compose.ui", "ui-tooling-preview").withoutVersion()
        library("compose-runtime-livedata", "androidx.compose.runtime", "runtime-livedata").withoutVersion()
        library("compose-ui-tooling", "androidx.compose.ui", "ui-tooling").withoutVersion()

        library("accompanist-systemuicontroller", "com.google.accompanist", "accompanist-systemuicontroller").version("0.32.0")

        library("navigation-compose", "androidx.navigation", "navigation-compose").version("2.7.7")
        library("constraintlayout-compose", "androidx.constraintlayout", "constraintlayout-compose").version("1.0.1")
        library("activity-compose", "androidx.activity", "activity-compose").versionRef("activity")

        bundle(
            "compose-library", listOf(
                "compose-material",
                "compose-material3",
                "compose-ui",
                "compose-ui-tooling-preview",
                "compose-runtime-livedata",
                "constraintlayout-compose",
                "navigation-compose",
                "activity-compose",
                "compose-coil",
            )
        )

        library("junit", "junit", "junit").version("4.13.2")
    }
}
val androidSourcesJar = task<Jar>("androidSourcesJar") {
    // 如果有Kotlin那么就需要打入dir
    if (project.hasProperty("kotlin")) {
        println("====> project kotlin")
        from(android.sourceSets.getByName("main").java.srcDirs)
    } else if (project.hasProperty("android")) {
        println("====> project java")
        from(android.sourceSets.getByName("main").java.srcDirs)
    } else {
        println("====> project java & kotlin")
        from(sourceSets.getByName("main").allSource)
    }
    archiveClassifier.set("sources")
    exclude("**/R.class")
    exclude("**/BuildConfig.class")
}
artifacts {
    archives(androidSourcesJar)
}
publishing {
    publications {
        create<MavenPublication>("baseLibMaven") {
            groupId = "io.github.chawloo"
            artifactId = "QuickBaseLib"
            version = rootProject.extra["version"].toString()
            artifact("$buildDir/outputs/aar/${project.name}-release.aar")
            artifact(androidSourcesJar)
            pom {
                name.set("QuickBaseLib")
                description.set("A base library for daily development")
                url.set("https://github.com/ChawLoo/QuickBaseLib")
                inceptionYear.set("2022")
                scm {
                    url.set("https://github.com/ChawLoo/QuickBaseLib")
                    connection.set("scm:git:https://github.com/ChawLoo/QuickBaseLib.git")
                    developerConnection.set("scm:git:https://github.com/ChawLoo/QuickBaseLib.git")
                }
                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                        comments.set("A business-friendly OSS license")
                    }
                }
                developers {
                    developer {
                        id.set("ChawLoo")
                        name.set("ChawLoo")
                        email.set("ChawLoo0827@qq.com")
                        url.set("https://github.com/ChawLoo/QuickBaseLib")
                    }
                }
                issueManagement {
                    system.set("Github")
                    url.set("https://github.com/ChawLoo/QuickBaseLib")
                }
                withXml {
                    val dependenciesNode = asNode().appendNode("dependencies")
                    project.configurations.all {
                        val name = this.name
                        if (name != "implementation" && name != "compile" && name != "api") {
                            return@all
                        }
                        println(this)
                        dependencies.forEach {
                            println(it)
                            if (it.name == "unspecified") {
                                // 忽略无法识别的
                                return@forEach
                            }
                            val dependencyNode = dependenciesNode.appendNode("dependency")
                            dependencyNode.appendNode("groupId", it.group)
                            dependencyNode.appendNode("artifactId", it.name)
                            dependencyNode.appendNode("version", it.version)
                            if (name == "api" || name == "compile") {
                                dependencyNode.appendNode("scope", "compile")
                            } else { // implementation
                                dependencyNode.appendNode("scope", "runtime")
                            }
                        }
                    }
                }
            }
        }
        create<MavenPublication>("versionControlMaven") {
            groupId = "io.github.chawloo"
            artifactId = "VersionControlPlugin"
            version = rootProject.extra["version"].toString()
            from(components["versionCatalog"])
            pom {
                name.set("VersionControlPlugin")
                description.set("Libraries VersionControl For Daily Development")
                url.set("https://github.com/ChawLoo/VersionControlPlugin")
                inceptionYear.set("2022")
                scm {
                    url.set("https://github.com/ChawLoo/VersionControlPlugin")
                    connection.set("scm:git:https://github.com/ChawLoo/VersionControlPlugin.git")
                    developerConnection.set("scm:git:https://github.com/ChawLoo/VersionControlPlugin.git")
                }
                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                        comments.set("A business-friendly OSS license")
                    }
                }
                developers {
                    developer {
                        id.set("ChawLoo")
                        name.set("ChawLoo")
                        email.set("ChawLoo0827@qq.com")
                        url.set("https://github.com/ChawLoo/VersionControlPlugin")
                    }
                }
                issueManagement {
                    system.set("Github")
                    url.set("https://github.com/ChawLoo/VersionControlPlugin")
                }
                withXml {
                    val dependenciesNode = asNode().appendNode("dependencies")
                    project.configurations.all {
                        val name = this.name
                        if (name != "implementation" && name != "compile" && name != "api") {
                            return@all
                        }
                        println(this)
                        dependencies.forEach {
                            println(it)
                            if (it.name == "unspecified") {
                                // 忽略无法识别的
                                return@forEach
                            }
                            val dependencyNode = dependenciesNode.appendNode("dependency")
                            dependencyNode.appendNode("groupId", it.group)
                            dependencyNode.appendNode("artifactId", it.name)
                            dependencyNode.appendNode("version", it.version)
                            if (name == "api" || name == "compile") {
                                dependencyNode.appendNode("scope", "compile")
                            } else { // implementation
                                dependencyNode.appendNode("scope", "runtime")
                            }
                        }
                    }
                }
            }
        }
    }
    repositories {
        maven {
            val releaseRepoUrl = uri("https://packages.aliyun.com/maven/repository/2418478-release-GhmPUt/")
            val snapshotRepoUrl = uri("https://packages.aliyun.com/maven/repository/2418478-snapshot-CGlsbs/")
            url = if (version.toString().endsWith("SNAPSHOT")) {
                snapshotRepoUrl
            } else {
                releaseRepoUrl
            }
            credentials {
                username = "609399173a10edbf367f5264"
                password = "=RTs0bvMruGT"
            }
        }
    }
}
signing {
    sign(publishing.publications)
}