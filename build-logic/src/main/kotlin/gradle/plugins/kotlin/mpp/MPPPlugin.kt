package gradle.plugins.kotlin.mpp

import com.github.ajalt.colormath.model.Ansi16
import gradle.api.configureEach
import gradle.api.file.replace
import gradle.api.project.ProjectLayout
import gradle.api.project.kotlin
import gradle.api.project.projectScript
import gradle.api.project.sourceSetsToComposeResourcesDirs
import klib.data.type.ansi.Attribute
import klib.data.type.ansi.ansiSpan
import klib.data.type.collections.toTreeString
import klib.data.type.primitives.string.addPrefixIfNotEmpty
import klib.data.type.primitives.string.lowercaseFirstChar
import klib.data.type.primitives.string.case.splitToWords
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget

private val TEST_SOURCE_SET_NAME_PREFIXES = listOf(
    SourceSet.TEST_SOURCE_SET_NAME,
    "unitTest",
    "instrumentedTest",
)

public class MPPPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
                adjustSourceSets()
                registerPrintHierarchyTemplateTask()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun Project.adjustSourceSets() {
        kotlin {
            when (val layout = projectScript.layout) {
                is ProjectLayout.Flat -> {
                    val androidTargets = kotlin.targets.filterIsInstance<KotlinAndroidTarget>()

                    kotlin.sourceSets.configureEach { sourceSet ->
                        var targetPart: String
                        var srcPrefixPart: String
                        var resourcesPrefixPart: String

                        val androidTarget =
                            androidTargets.filter { target -> sourceSet.name.startsWith(target.targetName) }
                                .maxByOrNull { target -> target.name.length }

                        if (androidTarget != null) {
                            targetPart = "${layout.targetDelimiter}${androidTarget.targetName}"

                            val restPart = sourceSet.name.removePrefix(androidTarget.targetName).lowercaseFirstChar()

                            if (restPart == androidTarget.mainVariant.sourceSetTree.get().name) {
                                srcPrefixPart = "src"
                                resourcesPrefixPart = ""
                            }
                            else {
                                val prefix = TEST_SOURCE_SET_NAME_PREFIXES.find(restPart::startsWith)

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

                            val sourceSetNameParts =
                                "(.*[a-z\\d])([A-Z]\\w+)$".toRegex().matchEntire(sourceSet.name)!!.groupValues

                            targetPart = sourceSetNameParts[1].let { targetName ->
                                if (targetName == "common") "" else "${layout.targetDelimiter}$targetName"
                            }

                            val compilationName = sourceSetNameParts[2].lowercaseFirstChar()

                            if (compilationName == KotlinCompilation.MAIN_COMPILATION_NAME) {
                                srcPrefixPart = "src"
                                resourcesPrefixPart = ""
                            }
                            else {
                                srcPrefixPart = compilationName
                                resourcesPrefixPart = compilationName
                            }
                        }

                        sourceSet.kotlin.replace(
                            "src/${sourceSet.name}/kotlin",
                            "$srcPrefixPart$targetPart",
                        )
                        sourceSet.resources.replace(
                            "src/${sourceSet.name}/resources",
                            "${resourcesPrefixPart}Resources$targetPart".lowercaseFirstChar(),
                        )
                        sourceSetsToComposeResourcesDirs[sourceSet] = project.layout.projectDirectory.dir("${resourcesPrefixPart}ComposeResources$targetPart".lowercaseFirstChar())
                    }
                }

                else -> Unit
            }
        }
    }

    private fun Project.registerPrintHierarchyTemplateTask() =
        tasks.register("printHierarchyTemplate") {
            group = "help"
            description = "Prints Kotlin source set hierarchy"

            doLast {
                val dependees: Map<String, List<String>> = kotlin.sourceSets
                    .associate { sourceSet -> sourceSet.name to emptyList<String>() }
                    .toMutableMap()
                    .apply {
                        kotlin.sourceSets.forEach { sourceSet ->
                            sourceSet.dependsOn.forEach { parentSourceSet ->
                                val children = getOrDefault(parentSourceSet.name, emptyList())
                                put(parentSourceSet.name, children + sourceSet.name)
                            }
                        }
                    }

                val commonMain = KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME
                project.logger.lifecycle("Kotlin SourceSet Dependees Hierarchy from commonMain:")
                project.logger.lifecycle(
                    commonMain.toTreeString(dependees) {
                        last().ansiSpan {
                            attribute(Attribute.INTENSITY_BOLD)
                            attribute(Ansi16(31 + size))
                        }
                    },
                )

                // Print hierarchies from other roots (if any).
                val all = kotlin.sourceSets.map(KotlinSourceSet::getName).toSet()
                val children = dependees.values.flatten().toSet()
                val roots = all - children
                roots.filter { root -> root != commonMain }.forEach { root ->
                    project.logger.lifecycle("Kotlin SourceSet Dependees Hierarchy from $root:")
                    project.logger.lifecycle(
                        root.toTreeString(dependees) {
                            last().ansiSpan {
                                attribute(Attribute.INTENSITY_BOLD)
                                attribute(Ansi16(31 + size))
                            }
                        },
                    )
                }
            }
        }
}
