@file:Suppress("UnstableApiUsage")

import java.util.Date

rootProject.name = "build-logic"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://packages.jetbrains.team/maven/p/amper/amper")
        maven("https://www.jetbrains.com/intellij-repository/releases")
        maven("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies")
    }
}


plugins {
    // Gives the data to speed up your build, improve build reliability and accelerate build debugging.
    id("com.gradle.develocity") version "3.19.1"
    // Enhances published build scans by adding a set of tags, links and custom values that have proven to be useful for many projects building with Develocity.
    id("com.gradle.common-custom-user-data-gradle-plugin") version "2.1"
}

develocity {
    val startParameter = gradle.startParameter
    val scanJournal = File(settingsDir, "scan-journal.log")

    server = "https://ge.solutions-team.gradle.com"

    buildScan {
        termsOfUseUrl = "https://gradle.com/help/legal-terms-of-use"
        termsOfUseAgree = "yes"
        uploadInBackground = true

        // obfuscate NIC since we don't want to expose user real IP (will be relevant without VPN)
        obfuscation {
            ipAddresses { addresses -> addresses.map { "0.0.0.0" } }
        }

        capture {
            fileFingerprints = true
        }

        buildScanPublished {
            scanJournal.appendText("${Date()} — $buildScanUri — $startParameter\n")
        }

        publishing.onlyIf { it.isAuthenticated }
    }
}


dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.gradle.org/gradle/libs-releases/")
        maven("https://packages.jetbrains.team/maven/p/amper/amper")
        maven("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies")
        maven("https://www.jetbrains.com/intellij-repository/releases")
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("io.github.z4kn4fein:semver:2.0.0")
    }
}
