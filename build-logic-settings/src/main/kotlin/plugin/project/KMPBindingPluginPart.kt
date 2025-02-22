@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project

import app.cash.sqldelight.core.decapitalize
import gradle.all
import gradle.kotlin
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.extraProperties

internal class KMPBindingPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (settings.projectProperties.kotlin.targets.isEmpty()) {
                return@with
            }

            plugins.apply(KotlinMultiplatformPluginWrapper::class.java)

            // Enable Default Kotlin Hierarchy.
            extraProperties.set("kotlin.mpp.applyDefaultHierarchyTemplate", "true")

            // IOS Compose uses UiKit, so we need to explicitly enable it, since it is experimental.
            extraProperties.set("org.jetbrains.compose.experimental.uikit.enabled", "true")

            adjustTargets()
            adjustSourceSets()

            // Workaround for KTIJ-27212, to get proper compiler arguments in the common code facet after import.
            // Apparently, configuring compiler arguments for metadata compilation is not sufficient.
            // This workaround doesn't fix intermediate source sets, though, only the most common fragment.
//        kotlinMPE.compilerOptions {
//            configureFrom(module.mostCommonFragment.settings)
//        }

//        val hasJUnit5 = module.fragments
//            .any { it.settings.junit == JUnitVersion.JUNIT5 }
//        val hasJUnit = module.fragments
//            .any { it.settings.junit != JUnitVersion.NONE }
//        if (hasJUnit) {
//            project.configurations.all { config ->
//                config.resolutionStrategy.capabilitiesResolution.withCapability("org.jetbrains.kotlin:kotlin-test-framework-impl") { capability ->
//                    val selected = if (hasJUnit5) {
//                        capability.candidates
//                            .filter { (it.id as? ModuleComponentIdentifier)?.module == "kotlin-test-junit5" }
//                            .firstOrNull { (it.id as? ModuleComponentIdentifier)?.version == UsedVersions.kotlinVersion }
//                            ?: error(
//                                "No kotlin test junit5 dependency variant on classpath. Existing: " +
//                                    capability.candidates.joinToString { it.variantName },
//                            )
//                    }
//                    else {
//                        capability.candidates
//                            .firstOrNull { (it.id as? ModuleComponentIdentifier)?.version == UsedVersions.kotlinVersion }
//                            ?: error(
//                                "No kotlin test junit dependency variant on classpath. Existing: " +
//                                    capability.candidates.joinToString { it.variantName },
//                            )
//                    }
//
//                    capability.select(selected)
//                    capability.because("Select junit impl, since it is embedded")
//                }
//            }
//        }
        }
    }

    /**
     * Set Amper specific directory layout.
     */
//    private fun adjustSourceSetDirectories() {
//        kotlinMPE.sourceSets.all { sourceSet ->
//            val fragment = sourceSet.amperFragment
//            when {
//                // Do GRADLE_JVM specific.
//                layout == Layout.GRADLE_JVM -> {
//                    if (sourceSet.name == "jvmMain") {
//                        replacePenultimatePaths(sourceSet.kotlin, sourceSet.resources, "main")
//                    }
//                    else if (sourceSet.name == "jvmTest") {
//                        replacePenultimatePaths(sourceSet.kotlin, sourceSet.resources, "test")
//                    }
//                }
//
//                // Do AMPER specific.
//                layout == Layout.AMPER && fragment != null -> {
//                    sourceSet.kotlin.tryAdd(fragment.src).tryRemove { it.endsWith("kotlin") }
//                    sourceSet.resources.tryAdd(fragment.resourcesPath).tryRemove { it.endsWith("resources") }
//                }
//
//                layout == Layout.AMPER && fragment == null -> {
//                    sourceSet.kotlin.setSrcDirs(emptyList<File>())
//                    sourceSet.resources.setSrcDirs(emptyList<File>())
//                }
//            }
//        }
//    }

//
//        // Skip tests binary creation for now.
//        module.leafFragments.forEach { fragment ->
//            val target = fragment.target ?: return@forEach
//            with(target) target@{
//                if (fragment.platform != Platform.ANDROID) {
//                    fragment.maybeCreateCompilation {
//                        if (this@target is KotlinNativeTarget)
//                            adjust(
//                                this@target,
//                                this as KotlinNativeCompilation,
//                                fragment,
//                            )
//                    }
//                }
//            }
//        }
//    }

    private fun Project.adjustTargets() =
        kotlin.applyDefaultHierarchyTemplate {
            common {
                settings.projectProperties.kotlin.targetGroups?.forEach { (name, group) ->
                    group(name) {
                        group.forEach { targetName ->
                            when (targetName) {
                                "jvm" -> withJvm()
                                "android" -> withAndroidTarget()
                                "ios" -> group("ios") {
                                    withIos()
                                }

                                "iosArm64" -> withIosArm64()
                                "iosX64" -> withIosX64()
                                "iosSimulatorArm64" -> withIosSimulatorArm64()
                                "js" -> withJs()
                                "wasm" -> withWasmJs()
                                else -> group(targetName)
                            }
                        }
                    }
                }
            }
        }

    @Suppress("UNCHECKED_CAST")
    private fun Project.adjustSourceSets() {
        kotlin {
            if (settings.projectProperties.flatLayout) {
                sourceSets.all { sourceSet ->
                    val sourceSetNameParts = "^(.*?)(Main|Test|TestDebug)?$".toRegex().matchEntire(sourceSet.name)!!

                    val (kotlinPrefixPart, resourcesPrefixPart) = sourceSetNameParts.groupValues[2].decapitalize().let {
                        when (it) {
                            "main", "" -> "src" to "resources"
                            else -> it to "${it}Resources"
                        }
                    }
                    val suffixPart = sourceSetNameParts.groupValues[1].let {
                        if (it == "common") "" else "@$it"
                    }
                    sourceSet.kotlin.setSrcDirs(listOf("$kotlinPrefixPart$suffixPart"))
                    sourceSet.resources.setSrcDirs(listOf("$resourcesPrefixPart$suffixPart"))
                }
            }

            sourceSets.forEach { sourceSet ->
                settings.projectProperties.kotlin.sourceSets?.get(sourceSet.name)?.dependencies?.let { dependencies ->
                    sourceSet.dependencies {
                        dependencies.forEach { dependency -> dependency.applyTo(this) }
                    }
                }
            }
        }
    }
}
