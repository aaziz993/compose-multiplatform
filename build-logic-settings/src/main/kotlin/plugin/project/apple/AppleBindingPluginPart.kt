/*
 * Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package plugin.project.apple

import gradle.kotlin
import gradle.moduleProperties
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import plugin.project.BindingPluginPart
import plugin.project.model.hasApple
import plugin.project.model.iosArm64
import plugin.project.model.iosSimulatorArm64
import plugin.project.model.iosX64
import plugin.project.model.macosArm64
import plugin.project.model.macosX64
import plugin.project.model.tvosArm64
import plugin.project.model.tvosSimulatorArm64
import plugin.project.model.tvosX64
import plugin.project.model.watchosArm32
import plugin.project.model.watchosArm64
import plugin.project.model.watchosDeviceArm64
import plugin.project.model.watchosSimulatorArm64

internal class AppleBindingPluginPart(override val project: Project) : BindingPluginPart {

    private val kotlinMPE: KotlinMultiplatformExtension = project.kotlin

    override val needToApply by lazy { project.moduleProperties.targets.hasApple }

    override fun applyBeforeEvaluate() = with(project) {
        // Apply plugin
        plugins.apply("org.jetbrains.gradle.apple.applePlugin")

        moduleProperties.targets.iosArm64.forEach { target -> target.applyTo(kotlinMPE) }
        moduleProperties.targets.iosX64.forEach { target -> target.applyTo(kotlinMPE) }
        moduleProperties.targets.iosSimulatorArm64.forEach { target -> target.applyTo(kotlinMPE) }
        moduleProperties.targets.watchosArm32.forEach { target -> target.applyTo(kotlinMPE) }
        moduleProperties.targets.watchosArm64.forEach { target -> target.applyTo(kotlinMPE) }
        moduleProperties.targets.watchosDeviceArm64.forEach { target -> target.applyTo(kotlinMPE) }
        moduleProperties.targets.watchosSimulatorArm64.forEach { target -> target.applyTo(kotlinMPE) }
        moduleProperties.targets.tvosArm64.forEach { target -> target.applyTo(kotlinMPE) }
        moduleProperties.targets.tvosX64.forEach { target -> target.applyTo(kotlinMPE) }
        moduleProperties.targets.tvosSimulatorArm64.forEach { target -> target.applyTo(kotlinMPE) }
        moduleProperties.targets.macosArm64.forEach { target -> target.applyTo(kotlinMPE) }
        moduleProperties.targets.macosX64.forEach { target -> target.applyTo(kotlinMPE) }

        extraProperties.set("generateBuildableXcodeproj.skipKotlinFrameworkDependencies", "true")

        // Add ios App
//        applePE?.iosApp {
//            iosDeviceFragments[0].settings.ios.teamId?.let {
//                buildSettings.DEVELOPMENT_TEAM(it)
//            }
//            productInfo["UILaunchScreen"] = mapOf<String, Any>()
//        }

    }
}
