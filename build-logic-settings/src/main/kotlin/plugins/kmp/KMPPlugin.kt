package plugins.kmp

import gradle.accessors.id
import gradle.accessors.kotlin
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.accessors.sourceSetsToComposeResourcesDirs
import gradle.api.configureEach
import gradle.api.file.replace
import gradle.decapitalized
import gradle.prefixIfNotEmpty
import gradle.project.ProjectLayout
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget

internal class KMPPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.kotlin.takeIf { kotlin -> kotlin.targets.isNotEmpty() }?.let { kotlin ->
                plugins.apply(project.settings.libs.plugins.plugin("kotlin.multiplatform").id)

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
                ProjectLayout.FLAT -> {
                    val androidTargets = kotlin.targets.filterIsInstance<KotlinAndroidTarget>()
                    kotlin.sourceSets.configureEach { sourceSet ->
                        var targetPart: String
                        var srcPrefixPart: String
                        var resourcesPrefixPart: String

                        val androidTarget = androidTargets.find { target -> sourceSet.name.startsWith(target.targetName) }

                        if (androidTarget != null) {
                            targetPart = "@${androidTarget.targetName}"

                            val restPart = sourceSet.name.removePrefix(androidTarget.targetName).decapitalized()

                            val mainSourceSetNamePrefix = androidTarget.mainVariant.sourceSetTree.get().name

                            val testSourceSetNamePrefixes = listOf(
                                SourceSet.TEST_SOURCE_SET_NAME,
                                "unitTest",
                                androidTarget.instrumentedTestVariant.sourceSetTree.get().name,
                            )

                            if (restPart == mainSourceSetNamePrefix) {
                                srcPrefixPart = "src"
                                resourcesPrefixPart = ""
                            }
                            else {
                                val prefix = testSourceSetNamePrefixes.find { prefix -> restPart.startsWith(prefix) }

                                if (prefix == null) {
                                    srcPrefixPart = restPart
                                    resourcesPrefixPart = srcPrefixPart
                                }
                                else {
                                    srcPrefixPart =
                                        "$prefix${restPart.removePrefix(prefix).prefixIfNotEmpty("+")}"
                                    resourcesPrefixPart = srcPrefixPart
                                }
                            }
                        }
                        else {

                            val sourceSetNameParts = "(.*[a-z\\d])([A-Z]\\w+)$".toRegex().matchEntire(sourceSet.name)!!

                            targetPart = sourceSetNameParts.groupValues[1].let { targetName ->
                                if (targetName == "common") "" else "@$targetName"
                            }

                            val compilationName = sourceSetNameParts.groupValues[2].decapitalized()

                            if (compilationName == KotlinCompilation.MAIN_COMPILATION_NAME) {
                                srcPrefixPart = "src"
                                resourcesPrefixPart = ""
                            }
                            else {
                                srcPrefixPart = compilationName
                                resourcesPrefixPart = compilationName
                            }
                        }

                        sourceSet.kotlin.replace("src/${sourceSet.name}/kotlin", "$srcPrefixPart$targetPart")
                        sourceSet.resources.replace("src/${sourceSet.name}/resources", "${resourcesPrefixPart}Resources$targetPart".decapitalized())
                        sourceSetsToComposeResourcesDirs[sourceSet] = layout.projectDirectory.dir("${resourcesPrefixPart}ComposeResources$targetPart".decapitalized())
                    }
                }

                else -> Unit
            }
        }
    }
}
