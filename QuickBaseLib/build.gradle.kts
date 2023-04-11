plugins {
    id("com.android.library")
    kotlin("android")
    `maven-publish`
    signing
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
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

    viewBinding {
        enable = true
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
    api(libs.activity.ktx)
    api(libs.fragment.ktx)
    api(libs.viewmodel)
    api(libs.livedata)
    api(libs.annotation)
    api(libs.constraintlayout)
    api(libs.recyclerview)
    api(libs.startup.runtime)
    api(libs.bundles.room)


    api(libs.material)
    api(libs.brv)
    api(libs.jodatime)
    api(libs.arouter.api)
    api(libs.androidautosize)
    api(libs.basePopup)
    api(libs.toast)
    api(libs.koin.android)
    api(libs.okhttp)
    api(libs.retrofit)
    api(libs.xPermission)
    api(libs.mmkv)
    api(libs.viewbinding.ktx)
    api(libs.jodatime)
    api(libs.wechat.sdk.android.without.mta)
    api(libs.wheelView)
    api(libs.x5webview)
    api(libs.banner)

    api(libs.bundles.coil)
    api(libs.bundles.saf.log)
    api(libs.bundles.immersionbar)

    testImplementation(libs.junit)
}

val androidSourcesJar = task<Jar>("androidSourcesJar") {
    archiveClassifier.set("sources")
    from(android.sourceSets["main"].java.srcDirs)
    exclude("**/R.class")
    exclude("**/BuildConfig.class")
}

publishing {
    val ver = "1.1.16"
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.github.chawloo"
            artifactId = "QuickBaseLib"
            version = ver
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
    }
    repositories {
        maven {
            val releaseRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
            url = if (ver.endsWith("SNAPSHOT")) {
                snapshotRepoUrl
            } else {
                releaseRepoUrl
            }
            credentials {
                username = rootProject.properties["maven.username"].toString()
                val pwd = rootProject.properties["maven.password"].toString()
                password = pwd
            }
        }
    }
}

signing {
    sign(publishing.publications)
}


