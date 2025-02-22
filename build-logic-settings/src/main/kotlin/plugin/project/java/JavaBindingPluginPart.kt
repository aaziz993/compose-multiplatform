/*
 * Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package plugin.project.java

import gradle.kotlin
import gradle.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin

internal class JavaBindingPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
//            if (TargetType.ANDROID in projectProperties.kotlin.jvm==null) {
//                logger.warn(
//                    "Cant enable java integration when android is enabled. " +
//                        "Module: $name",
//                )
//                return@with
//            }

            projectProperties.kotlin.jvm?.forEach { targetName, target ->
                targetName.takeIf(String::isNotEmpty)?.also { targetName ->
                    kotlin.jvm(targetName) {
                        target.applyTo(this)
                    }
                } ?: kotlin.jvm {
                    target.applyTo(this)
                }
            } ?: return

//            configureJavaExtension()
//
//            if (projectProperties.application && projectProperties.compose.enabled) {
//                plugins.apply(ApplicationPlugin::class.java)
//                configureJavaApplication()
//            }

//            configureJar()
        }
    }
}
