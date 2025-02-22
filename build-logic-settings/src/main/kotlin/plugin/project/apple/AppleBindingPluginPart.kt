/*
 * Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package plugin.project.apple

import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import plugin.project.kotlin.model.target.TargetType
import plugin.project.kotlin.model.target.applyTo
import plugin.project.kotlin.model.target.contains

internal class AppleBindingPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (TargetType.APPLE !in projectProperties.kotlin.targets) {
                return@with
            }

            plugins.apply(libs.plugins.apple.get().pluginId)

           projectProperties.kotlin.targets.filter { (type, _) -> type.isDescendantOf(TargetType.APPLE) }
                .forEach { target -> target.applyTo() }

            extraProperties.set("generateBuildableXcodeproj.skipKotlinFrameworkDependencies", "true")

            configureAppleProjectExtension()
        }
    }
}
