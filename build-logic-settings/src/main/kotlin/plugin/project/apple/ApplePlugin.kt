/*
 * Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package plugin.project.apple

import gradle.id
import gradle.kotlin
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.extraProperties

internal class ApplePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (!projectProperties.kotlin.hasAppleTargets) {
                return@with
            }

            plugins.apply(settings.libs.plugins.plugin("apple").id)
            plugins.apply(settings.libs.plugins.plugin("cocoapods").id)

            extraProperties.set("generateBuildableXcodeproj.skipKotlinFrameworkDependencies", "true")

            configureAppleProjectExtension()

            projectProperties.kotlin.iosArm64?.forEach { targetName, target ->
                if (targetName.isNotEmpty()) {
                    kotlin.iosArm64(targetName) {
                        target.applyTo(this)
                    }
                }
                else kotlin.iosArm64 {
                    target.applyTo(this)
                }
            }

            projectProperties.kotlin.iosX64?.forEach { targetName, target ->
                if (targetName.isNotEmpty()) {
                    kotlin.iosX64(targetName) {
                        target.applyTo(this)
                    }
                }
                else kotlin.iosX64 {
                    target.applyTo(this)
                }
            }

            projectProperties.kotlin.iosSimulatorArm64?.forEach { targetName, target ->
                if (targetName.isNotEmpty()) {
                    kotlin.iosSimulatorArm64(targetName) {
                        target.applyTo(this)
                    }
                }
                else kotlin.iosSimulatorArm64 {
                    target.applyTo(this)
                }
            }

            projectProperties.kotlin.watchosArm32?.forEach { targetName, target ->
                if (targetName.isNotEmpty()) {
                    kotlin.watchosArm32(targetName) {
                        target.applyTo(this)
                    }
                }
                else kotlin.watchosArm32 {
                    target.applyTo(this)
                }
            }

            projectProperties.kotlin.watchosArm64?.forEach { targetName, target ->
                if (targetName.isNotEmpty()) {
                    kotlin.watchosArm64(targetName) {
                        target.applyTo(this)
                    }
                }
                else kotlin.watchosArm64 {
                    target.applyTo(this)
                }
            }

            projectProperties.kotlin.watchosX64?.forEach { targetName, target ->
                if (targetName.isNotEmpty()) {
                    kotlin.watchosX64(targetName) {
                        target.applyTo(this)
                    }
                }
                else kotlin.watchosX64 {
                    target.applyTo(this)
                }
            }

            projectProperties.kotlin.watchosSimulatorArm64?.forEach { targetName, target ->
                if (targetName.isNotEmpty()) {
                    kotlin.watchosSimulatorArm64(targetName) {
                        target.applyTo(this)
                    }
                }
                else kotlin.watchosSimulatorArm64 {
                    target.applyTo(this)
                }
            }

            projectProperties.kotlin.tvosArm64?.forEach { targetName, target ->
                if (targetName.isNotEmpty()) {
                    kotlin.tvosArm64(targetName) {
                        target.applyTo(this)
                    }
                }
                else kotlin.tvosArm64 {
                    target.applyTo(this)
                }
            }

            projectProperties.kotlin.tvosX64?.forEach { targetName, target ->
                if (targetName.isNotEmpty()) {
                    kotlin.tvosX64(targetName) {
                        target.applyTo(this)
                    }
                }
                else kotlin.tvosX64 {
                    target.applyTo(this)
                }
            }

            projectProperties.kotlin.tvosSimulatorArm64?.forEach { targetName, target ->
                if (targetName.isNotEmpty()) {
                    kotlin.tvosSimulatorArm64(targetName) {
                        target.applyTo(this)
                    }
                }
                else kotlin.tvosSimulatorArm64 {
                    target.applyTo(this)
                }
            }

            projectProperties.kotlin.macosArm64?.forEach { targetName, target ->
                if (targetName.isNotEmpty()) {
                    kotlin.macosArm64(targetName) {
                        target.applyTo(this)
                    }
                }
                else kotlin.macosArm64 {
                    target.applyTo(this)
                }
            }

            projectProperties.kotlin.macosX64?.forEach { targetName, target ->
                if (targetName.isNotEmpty()) {
                    kotlin.macosX64(targetName) {
                        target.applyTo(this)
                    }
                }
                else kotlin.macosX64 {
                    target.applyTo(this)
                }
            }

            configureCocoapodsExtension()
        }
    }
}
