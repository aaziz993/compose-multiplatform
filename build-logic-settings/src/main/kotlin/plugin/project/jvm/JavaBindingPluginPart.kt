/*
 * Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package plugin.project.jvm

import gradle.moduleProperties
import org.gradle.api.Project
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.plugins.JavaPluginExtension
import plugin.project.BindingPluginPart
import plugin.project.model.target.TargetType
import plugin.project.model.target.add
import plugin.project.model.target.contains
import plugin.project.model.target.isDescendantOf

/**
 * Plugin logic, bind to specific module, when only default target is available.
 */
internal class JavaBindingPluginPart(override val project: Project) : BindingPluginPart {

    private val javaAPE: JavaApplication? get() = project.extensions.findByType(JavaApplication::class.java)
    internal val javaPE: JavaPluginExtension get() = project.extensions.getByType(JavaPluginExtension::class.java)

    override val needToApply by lazy {
        if (TargetType.ANDROID in project.moduleProperties.targets) {
            project.logger.warn(
                "Cant enable java integration when android is enabled. " +
                    "Module: ${project.name}",
            )
            return@lazy false
        }
       TargetType.JVM in project.moduleProperties.targets
    }

    override fun applyBeforeEvaluate() = with(project) {
        moduleProperties.targets
            .filter { target -> target.type.isDescendantOf(TargetType.JVM) }
            .forEach { target->target.add() }


//        adjustJavaGeneralProperties()

//        addJavaIntegration()
    }

//    override fun applyAfterEvaluate() {
////        adjustSourceDirs()
//    }
//
//    private fun adjustJavaGeneralProperties() = with(project) {
//        if (leafPlatformFragments.size > 1)
//        // TODO Add check that all parts values are the same instead of this approach.
//            logger.info(
//                "Cant apply multiple settings for application plugin. " +
//                    "Affected artifacts: ${platformArtifacts.joinToString { it.name }}. " +
//                    "Applying application settings from first one.",
//            )
//        val fragment = leafPlatformFragments.firstOrNull() ?: return
//        if (!fragment.settings.compose.enabled) {
//            project.plugins.apply(ApplicationPlugin::class.java)
//        }
//
//        val jvmSettings = fragment.settings.jvm
//        jvmSettings.release?.let { release ->
//            project.tasks.withType(JavaCompile::class.java).configureEach {
////                it.options.release.set(release.releaseNumber)
//            }
//        }
//
//        // Do when layout is known.
//        project.afterEvaluate {
//            if (module.type.isLibrary()) return@afterEvaluate
////            val foundMainClass = if (jvmSettings.mainClass != null) {
////                jvmSettings.mainClass
////            }
////            else {
////                val sources = fragment.kotlinSourceSet?.closureSources?.ifEmpty {
////                    val kotlinSourceSet = fragment.kotlinSourceSet
////                    (kotlinSourceSet as DefaultKotlinSourceSet).compilations.flatMap {
////                        it.defaultSourceSet.kotlin.srcDirs
////                    }.map { it.toPath() }
////                } ?: emptyList()
////
////                findEntryPoint(sources, EntryPointType.JVM, logger)
////            }
////
////            @Suppress("OPT_IN_USAGE")
////            (fragment.target as KotlinJvmTarget).mainRun {
////                mainClass.set(foundMainClass)
////            }
//
//            javaAPE?.apply {
//                // Check if main class is set in the build script.
////                if (mainClass.orNull == null) {
////                    mainClass.set(foundMainClass)
////                }
//            }
//            // TODO Handle Amper variants gere, when we will switch to manual java source sets creation.
//            project.tasks.withType(Jar::class.java) {
////                it.manifest {
////                    it.attributes["Main-Class"] = foundMainClass
////                }
//            }
//        }
//    }
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
