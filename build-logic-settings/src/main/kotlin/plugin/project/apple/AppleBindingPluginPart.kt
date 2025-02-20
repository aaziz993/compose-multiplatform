/*
 * Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package plugin.project.apple

import gradle.moduleProperties
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import plugin.project.BindingPluginPart
import plugin.project.model.target.TargetType
import plugin.project.model.target.add
import plugin.project.model.target.contains
import plugin.project.model.target.isDescendantOf

internal class AppleBindingPluginPart(override val project: Project) : BindingPluginPart {

    override val needToApply by lazy { TargetType.APPLE in project.moduleProperties.targets }

    override fun applyBeforeEvaluate() = with(project) {
        // Apply plugin
        plugins.apply("org.jetbrains.gradle.apple.applePlugin")

        moduleProperties.targets
            .filter { (type, _) -> type.isDescendantOf(TargetType.APPLE) }
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
