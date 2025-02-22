/*
 * Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package plugin.project.jvm

import gradle.java
import gradle.javaApp
import gradle.moduleProperties
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import plugin.project.model.target.TargetType
import plugin.project.model.target.applyTo
import plugin.project.model.target.contains

/**
 * Plugin logic, bind to specific module, when only default target is available.
 */
internal class JavaBindingPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (TargetType.ANDROID in moduleProperties.targets) {
                logger.warn(
                    "Cant enable java integration when android is enabled. " +
                        "Module: $name",
                )
                return@with
            }

            if (TargetType.JVM !in moduleProperties.targets) {
                return@with
            }

            moduleProperties.targets.filter { target -> target.type.isDescendantOf(TargetType.JVM) }
                .forEach { target -> target.applyTo() }

//        adjustJavaGeneralProperties()

//        addJavaIntegration()
        }
    }


    private fun Project.adjustJavaGeneralProperties() {
        if (!moduleProperties.settings.compose.enabled) {
            project.plugins.apply(ApplicationPlugin::class.java)
        }

        val jvmSettings = moduleProperties.settings.jvm
//        jvmSettings.release?.let { release ->
//            project.tasks.withType(JavaCompile::class.java).configureEach {
////                it.options.release.set(release.releaseNumber)
//            }
//        }

        // Do when layout is known.
        project.afterEvaluate {
            if (!moduleProperties.application) return@afterEvaluate

            val foundMainClass = if (jvmSettings.mainClass != null) {
                jvmSettings.mainClass
            }
            else {

            }

            @Suppress("OPT_IN_USAGE")
            (fragment.target as KotlinJvmTarget).mainRun {
                mainClass.set(foundMainClass)
            }

            java  {
                // Check if main class is set in the build script.
                if (mainClass.orNull == null) {
                    mainClass.set(foundMainClass)
                }
            }
            // TODO Handle Amper variants gere, when we will switch to manual java source sets creation.
            project.tasks.withType(Jar::class.java) {
//                it.manifest {
//                    it.attributes["Main-Class"] = foundMainClass
//                }
            }
        }
    }
//
//    // TODO Rewrite this completely by not calling
//    //  KMPP code and following out own conventions.
//    private fun addJavaIntegration() {
//        project.plugins.apply(JavaPlugin::class.java)
//
//        kotlinMPE.targets.toList().forEach {
//            if (it is KotlinJvmTarget) it.withJava()
//        }
//
//        // Set sources for all Amper related source sets.
//        platformFragments.forEach {
////            it.maybeCreateJavaSourceSet()
//        }
//    }
}
