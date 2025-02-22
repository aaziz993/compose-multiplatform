/*
 * Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package plugin.project.java

import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import plugin.project.kotlin.model.target.TargetType
import plugin.project.kotlin.model.target.applyTo
import plugin.project.kotlin.model.target.contains

internal class JavaBindingPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (TargetType.ANDROID in projectProperties.kotlin.targets) {
                logger.warn(
                    "Cant enable java integration when android is enabled. " +
                        "Module: $name",
                )
                return@with
            }

            if (TargetType.JVM !in projectProperties.kotlin.targets) {
                return@with
            }

            projectProperties.kotlin.targets.filter { target -> target.type.isDescendantOf(TargetType.JVM) }
                .forEach { target -> target.applyTo() }

            configureJavaExtension()

            if (projectProperties.application && projectProperties.compose.enabled) {
                plugins.apply(ApplicationPlugin::class.java)
                configureJavaApplication()
            }

            configureJar()
        }
    }
}
