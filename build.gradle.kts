plugins {
    kotlin("multiplatform") version "1.5.31"// kotlin version
    id("convention.publication")
    id("com.android.library")
}

/*
    Publish to maven
    https://dev.to/kotlin/how-to-build-and-publish-a-kotlin-multiplatform-library-going-public-4a8k
    https://getstream.io/blog/publishing-libraries-to-mavencentral-2021/#your-first-release

    Build and send
    0.  ./gradlew clean
    1.  ./gradlew publishAllPublicationsToSonatypeRepository

    Confirm
    1.  go to https://s01.oss.sonatype.org/#stagingRepositories
    2.  Find your repository in the ‘Staging repositories’ section.
    3.  Close it.
    4.  🚀 Release it!

 */

val versionMajor = 0
val versionMinor = 2
val versionPatch = 6
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