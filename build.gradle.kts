import org.jetbrains.kotlin.konan.properties.loadProperties
import java.util.*

plugins {
    kotlin("multiplatform") version "1.4.10"
    id("com.android.library")
    id("kotlin-android-extensions")
    id("maven-publish")
    id("com.jfrog.bintray") version "1.8.5"
}

val versionMajor = 0
val versionMinor = 1
val versionPatch = 0
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
    android{
        publishLibraryVariants("release", "debug")
    }
    iosX64("ios") {
        binaries {
            framework {
                baseName = "library"
            }
        }
    }
    iosArm64 {
        binaries {
            framework {
                baseName = "library"
            }
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
        val androidMain by getting {
            dependencies {
                implementation("androidx.core:core-ktx:1.3.2")
            }
        }
        val androidTest by getting
        val iosMain by getting
        val iosTest by getting
    }
}

android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = versionText
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

//      Publishing

val (bintrayUser, bintrayPass, bintrayKey) = project.rootProject.file("publish.properties").let {
    it.absolutePath
}.let { path ->
    loadProperties(path)
}.let{ prop ->
    val user = prop.getProperty("bintrayUser")
    val pass = prop.getProperty("bintrayPass")
    val key = prop.getProperty("bintrayKey")
    System.err.println("bintray credentials: $user/$pass key: $key")
    listOf(user, pass, key)
}

val artifactName = project.name
val artifactGroup = project.group.toString()
val artifactVersion = project.version.toString()

val pomUrl = "https://github.com/september669/KmmAnkoLogger"
val pomScmUrl = "https://github.com/september669/KmmAnkoLogger"
val pomIssueUrl = "https://github.com/september669/KmmAnkoLogger/issues"
val pomDesc = "https://github.com/september669/KmmAnkoLogger"

val githubRepo = "september669/KmmAnkoLogger"
val githubReadme = "README.md"

val pomLicenseName = "MIT"
val pomLicenseUrl = "https://opensource.org/licenses/mit-license.php"
val pomLicenseDist = "repo"

val pomDeveloperId = "september669"
val pomDeveloperName = "Denis dda"


publishing {
    publications {
        create<MavenPublication>("AnkoLogger") {
            groupId = artifactGroup
            artifactId = artifactName
            version = artifactVersion
            from(components["kotlin"])

            pom.withXml {
                asNode().apply {
                    appendNode("description", pomDesc)
                    appendNode("name", rootProject.name)
                    appendNode("url", pomUrl)
                    appendNode("licenses").appendNode("license").apply {
                        appendNode("name", pomLicenseName)
                        appendNode("url", pomLicenseUrl)
                        appendNode("distribution", pomLicenseDist)
                    }
                    appendNode("developers").appendNode("developer").apply {
                        appendNode("id", pomDeveloperId)
                        appendNode("name", pomDeveloperName)
                    }
                    appendNode("scm").apply {
                        appendNode("url", pomScmUrl)
                    }
                }
            }
        }
    }
}

bintray {
    user = bintrayUser
    key = bintrayKey
    publish = true

    setPublications("AnkoLogger")

    pkg.apply {
        repo = "KmmAnkoLogger"
        name = artifactName
        userOrg = "september669"
        githubRepo = "https://github.com/september669/KmmAnkoLogger"
        vcsUrl = pomScmUrl
        description = "Port of AnkoLogger to KMM"
        //setLabels("kotlin", "faker", "testing", "test-automation", "data", "generation")
        setLicenses("MIT")
        desc = description
        websiteUrl = pomUrl
        issueTrackerUrl = pomIssueUrl
        githubReleaseNotesFile = githubReadme

        version.apply {
            name = artifactVersion
            desc = pomDesc
            released = Date().toString()
            vcsTag = artifactVersion
        }
    }
}
