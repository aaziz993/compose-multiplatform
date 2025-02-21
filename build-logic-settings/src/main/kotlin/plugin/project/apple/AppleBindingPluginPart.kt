/*
 * Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package plugin.project.apple

import gradle.libs
import gradle.moduleProperties
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import plugin.project.model.target.TargetType
import plugin.project.model.target.applyTo
import plugin.project.model.target.contains

internal class AppleBindingPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (TargetType.APPLE !in moduleProperties.targets) {
                return@with
            }

            plugins.apply(libs.plugins.apple.get().pluginId)

            moduleProperties.targets.filter { (type, _) -> type.isDescendantOf(TargetType.APPLE) }
                .forEach { target -> target.applyTo() }

            extraProperties.set("generateBuildableXcodeproj.skipKotlinFrameworkDependencies", "true")

            configureAppleProjectExtension()
        }
    }
}
