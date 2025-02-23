@file:Suppress("UnstableApiUsage")

import java.util.*
import org.yaml.snakeyaml.Yaml

//apply(from = "gradle/model/toolchainmanagement/model/ToolchainManagementSettings.kt")

rootProject.name = "build-logic-settings"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

val projectSettings = Yaml().load<MutableMap<String, *>>(file("../project.yaml").readText())

val gradleProperties: Properties = Properties().apply {
    val file = file("../gradle.properties")
    if (file.exists()) {
        load(file.reader())
    }
}

val localProperties: Properties = Properties().apply {
    val file = file("../local.properties")
    if (file.exists()) {
        load(file.reader())
    }
}

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
    // Provides a repository for downloading JVMs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
    // Enforcing pre-commit and commit-msg Git hooks configuration. Conventional-commits-ready.
    id("org.danilopianini.gradle-pre-commit-git-hooks") version "2.0.4"
}

val isCI = System.getenv("CI") != null

develocity {
    val startParameter = gradle.startParameter
    val scanJournal = File(settingsDir, "scan-journal.log")

    server = gradleProperties.getProperty("develocity.server")

    buildScan {
        uploadInBackground = !isCI

        // obfuscate NIC since we don't want to expose user real IP (will be relevant without VPN)
        obfuscation {
            ipAddresses { addresses -> addresses.map { _ -> "0.0.0.0" } }
        }

        capture {
            fileFingerprints = true
        }

        buildScanPublished {
            scanJournal.appendText("${Date()} — $buildScanUri — $startParameter\n")
        }

        val skipBuildScans = gradleProperties.getProperty("develocity.skip-build-scans").toBoolean()

        publishing.onlyIf { it.isAuthenticated && !skipBuildScans }
    }
}

buildCache {

    if (isCI) {
        local {
            isEnabled = gradleProperties.getProperty("develocity.build.cache.local.enable").toBoolean()
        }
    }

    remote(develocity.buildCache) {
        isEnabled = gradleProperties.getProperty("develocity.build.cache.remote.enable").toBoolean()
        // Check access key presence to avoid build cache errors on PR builds when access key is not present
        val accessKey = System.getenv().getOrElse("GRADLE_ENTERPRISE_ACCESS_KEY") {
            localProperties.getProperty("gradle.enterprise.access-key")
        }
        isPush = isCI && accessKey != null
    }
}

toolchainManagement {
    jvm {
        javaRepositories {
//            repository("foojay") {
//                resolverClass.set(FoojayToolchainResolver::class.java)
//            }
        }
    }
}

gitHooks {

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
        apply(from = "gradle/model/test/test.kts")
        classpath("org.tomlj:tomlj:1.1.1")
        classpath("org.yaml:snakeyaml:2.3")
        classpath("io.github.z4kn4fein:semver:2.0.0")
    }
}
