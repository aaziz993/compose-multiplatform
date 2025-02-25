package plugin.project.kotlin.kmp

import app.cash.sqldelight.core.decapitalize
import gradle.id
import gradle.kotlin
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinMetadataTarget
import plugin.project.kotlin.kmp.model.KotlinMultiplatformSettings
import plugin.project.model.ProjectLayout

internal class KMPPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.kotlin.takeIf { it.targets?.isNotEmpty() == true }?.let { kotlin ->
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

    @Suppress("UNCHECKED_CAST")
    private fun Project.adjustSourceSets() {
        kotlin {
            when (projectProperties.layout) {
                ProjectLayout.FLAT -> targets.forEach { target ->
                    val targetPart = if (target is KotlinMetadataTarget) "" else "@${target.targetName}"
                    target.compilations.forEach { compilation ->
                        compilation.defaultSourceSet {
                            val srcPrefixPart = when (compilation.name) {
                                KotlinCompilation.MAIN_COMPILATION_NAME, "" -> "src"
                                else -> compilation.name
                            }

                            kotlin.setSrcDirs(listOf("$srcPrefixPart$targetPart"))
                            resources.setSrcDirs(listOf("${compilation.name}Resources$targetPart".decapitalize()))
                        }
                    }
                }

                else -> Unit
            }

            sourceSets.forEach { sourceSet ->
                projectProperties.kotlin.sourceSets?.find { it.name == sourceSet.name }?.applyTo(sourceSet)
            }
        }
    }
}
