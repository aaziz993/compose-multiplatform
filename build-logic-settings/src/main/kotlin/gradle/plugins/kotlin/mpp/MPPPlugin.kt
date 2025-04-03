package gradle.plugins.kotlin.mpp

import gradle.accessors.kotlin
import gradle.accessors.projectProperties
import gradle.accessors.sourceSetsToComposeResourcesDirs
import klib.data.type.primitive.addPrefixIfNotEmpty
import gradle.api.configureEach
import gradle.api.file.replace
import klib.data.type.primitive.decapitalized
import gradle.api.project.ProjectLayout
import net.pearx.kasechange.splitToWords
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget

internal class MPPPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Apply kotlin multiplatform properties.
            projectProperties.kotlin?.applyTo()

            pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
                adjustSourceSets()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun Project.adjustSourceSets() {
        kotlin {
            when (val layout = projectProperties.layout) {
                is ProjectLayout.Flat -> {
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
                                        .splitToWords()
                                        .let { words ->
                                            words.firstOrNull()
                                                .orEmpty() +
                                                words.drop(1)
                                                    .joinToString(layout.androidVariantDelimiter)
                                                    .addPrefixIfNotEmpty(layout.androidAllVariantsDelimiter)
                                        }
                                    resourcesPrefixPart = srcPrefixPart
                                }
                                else {
                                    srcPrefixPart =
                                        "$prefix${
                                            restPart
                                                .removePrefix(prefix)
                                                .splitToWords()
                                                .joinToString(layout.androidVariantDelimiter)
                                                .addPrefixIfNotEmpty(layout.androidAllVariantsDelimiter)
                                        }"
                                    resourcesPrefixPart = srcPrefixPart
                                }
                            }
                        }
                        else {

                            val sourceSetNameParts = "(.*[a-z\\d])([A-Z]\\w+)$".toRegex().matchEntire(sourceSet.name)!!

                            targetPart = sourceSetNameParts.groupValues[1].let { targetName ->
                                if (targetName == "common") "" else "${layout.targetDelimiter}$targetName"
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
                        sourceSetsToComposeResourcesDirs[sourceSet] = project.layout.projectDirectory.dir("${resourcesPrefixPart}ComposeResources$targetPart".decapitalized())
                    }
                }

                else -> Unit
            }
        }
    }
}
