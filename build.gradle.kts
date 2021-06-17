plugins {
    kotlin("multiplatform") version "1.4.30"
    id("convention.publication")
    id("com.android.library")
}

val versionMajor = 0
val versionMinor = 2
val versionPatch = 4
val versionNum = 10_000 * versionMajor + 100 * versionMinor + 1 * versionPatch
val versionText = "$versionMajor.$versionMinor.$versionPatch"


group = "io.github.september669"
version = versionText


repositories {
    gradlePluginPortal() // To use 'maven-publish' and 'signing' plugins in our own plugin
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

    ios {
        val main by compilations.getting
        val interop by main.cinterops.creating {
            defFile(project.file("src/nativeInterop/cinterop/interop.def"))
        }
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