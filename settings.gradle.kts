pluginManagement {
    includeBuild("build-logic-settings")
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven("https://packages.jetbrains.team/maven/p/amper/amper")
        maven("https://www.jetbrains.com/intellij-repository/releases")
        maven("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies")
    }
}

plugins {
    id("settings.convention")
}

include(":android-app")
include(":ios-app")
include(":jvm-app")
include(":wasm-app")
include(":server-app")
include(":shared")
include(":cmp-lib")
include(":kmp-lib")
include(":compiler")
