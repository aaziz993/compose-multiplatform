package plugin.project.kotlin.kmp

import app.cash.sqldelight.core.decapitalize
import gradle.all
import gradle.id
import gradle.kotlin
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import plugin.project.kotlin.kmp.model.KotlinMultiplatformSettings
import plugin.project.model.Layout

internal class KMPPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.kotlin.takeIf(KotlinMultiplatformSettings::hasTargets)?.let { kotlin ->
                plugins.apply(settings.libs.plugins.plugin("kotlin.multiplatform").id)

                // Enable Default Kotlin Hierarchy.
                extraProperties.set("kotlin.mpp.applyDefaultHierarchyTemplate", "true")

                // IOS Compose uses UiKit, so we need to explicitly enable it, since it is experimental.
                extraProperties.set("org.jetbrains.compose.experimental.uikit.enabled", "true")

                kotlin.applyTo()

                adjustSourceSets()
            }
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

    @Suppress("UNCHECKED_CAST")
    private fun Project.adjustSourceSets() {
        kotlin {
            when (projectProperties.layout) {
                Layout.FLAT -> sourceSets.all { sourceSet ->
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

                else -> Unit
            }

            sourceSets.forEach { sourceSet ->
                projectProperties.kotlin.sourceSets?.get(sourceSet.name)?.dependencies?.let { dependencies ->
                    sourceSet.dependencies {
                        dependencies.forEach { dependency -> dependency.applyTo(this) }
                    }
                }
            }
        }
    }
}
