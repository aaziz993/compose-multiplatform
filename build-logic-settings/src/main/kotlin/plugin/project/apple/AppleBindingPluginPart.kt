/*
 * Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package plugin.project.apple

import gradle.id
import gradle.libs
import gradle.moduleProperties
import gradle.plugin
import gradle.plugins
import gradle.settings
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.gradle.api.Plugin
import plugin.project.model.target.TargetType
import plugin.project.model.target.add
import plugin.project.model.target.contains

internal class AppleBindingPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            moduleProperties.targets?.let { targets ->
                if (TargetType.APPLE !in targets) {
                    return@with
                }

                plugins.apply(libs.plugins.apple.get().pluginId)

                targets.filter { (type, _) -> type.isDescendantOf(TargetType.APPLE) }
                    .forEach { target -> target.add() }

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
    }
}
