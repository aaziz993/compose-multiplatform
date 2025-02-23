/*
 * Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package plugin.project.java

import gradle.kotlin
import gradle.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.jvm.tasks.Jar
import org.jetbrains.amper.gradle.BindingProjectPlugin
import org.jetbrains.amper.gradle.java.JavaBindingPluginPart
import org.jetbrains.amper.gradle.kmpp.KMPPBindingPluginPart
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

internal class JavaPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.kotlin.jvm?.forEach { targetName, target ->
                targetName.takeIf(String::isNotEmpty)?.also { targetName ->
                    kotlin.jvm(targetName) {
                        target.applyTo(this)
                    }
                } ?: kotlin.jvm {
                    target.applyTo(this)
                }
            } ?: return

            if (projectProperties.kotlin.android != null) {
                logger.warn(
                    "Can't enable java integration when android is enabled. " +
                        "Project: $name",
                )
                return@with
            }

//            configureJavaExtension()
//
//            if (projectProperties.application && projectProperties.compose.enabled) {
//                plugins.apply(ApplicationPlugin::class.java)
//                configureJavaApplication()
//            }

//            configureJar()
        }
    }

    // TODO Rewrite this completely by not calling
    //  KMPP code and following out own conventions.
    private fun Project.addJavaIntegration() {
        plugins.apply(JavaPlugin::class.java)

        kotlin.targets.toList().forEach {
            if (it is KotlinJvmTarget) it.withJava()
        }

//        // Set sources for all Amper related source sets.
//        platformFragments.forEach {
//            it.maybeCreateJavaSourceSet()
//        }
    }
}
