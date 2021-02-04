plugins {
    kotlin("multiplatform") version "1.4.30"
    id("com.android.library")
    id("kotlin-android-extensions")
    id("maven-publish")
}

val versionMajor = 0
val versionMinor = 2
val versionPatch = 2
val versionNum = 10_000 * versionMajor + 100 * versionMinor + 1 * versionPatch
val versionText = "$versionMajor.$versionMinor.$versionPatch"

group = "org.dda.ankoLogger"
version = versionText


repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
}

kotlin {


    targets.all {
        compilations.all {
            kotlinOptions {
                allWarningsAsErrors = true
            }
        }
    }

    android {
        publishLibraryVariants("release", "debug")
    }


    val libName = "${project.name}_lib"
    val iosX64 = iosX64("ios") {
        binaries {
            framework {
                baseName = libName
            }
        }
    }
    val iosArm64 = iosArm64 {
        binaries {
            framework {
                baseName = libName
            }
        }
    }
    // Create a task to build a fat framework.
    tasks.create("debugFatFramework", org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask::class) {
        // The fat framework must have the same base name as the initial frameworks.
        baseName = libName
        // The default destination directory is '<build directory>/fat-framework'.
        destinationDir = buildDir.resolve("fat-framework/debug")
        // Specify the frameworks to be merged.
        from(
            iosX64.binaries.getFramework("DEBUG"),
            iosArm64.binaries.getFramework("DEBUG")
        )
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting
        val androidTest by getting
        val iosMain by getting
        val iosTest by getting
    }
}

android {
    compileSdkVersion(30)
    defaultConfig {
        minSdkVersion(20)
        targetSdkVersion(30)
        versionCode = versionNum
        versionName = versionText
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

//      Publishing
/*

val (bintrayUser, bintrayPass, bintrayKey) = project.rootProject.file("publish.properties").let {
    it.absolutePath
}.let { path ->
    loadProperties(path)
}.let { prop ->
    val user = prop.getProperty("bintrayUser")
    val pass = prop.getProperty("bintrayPass")
    val key = prop.getProperty("bintrayKey")
    System.err.println("bintray credentials: $user/$pass key: $key")
    listOf(user, pass, key)
}

publishing {
    repositories.maven("https://api.bintray.com/maven/september669/KmmAnkoLogger/AnkoLogger/;publish=1;override=1") {
        name = "bintray"

        credentials {
            username = bintrayUser
            password = bintrayKey
        }
    }
}
*/
