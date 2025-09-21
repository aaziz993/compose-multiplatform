package gradle.plugins.kotlin.mpp

import com.github.ajalt.colormath.model.Ansi16
import gradle.api.file.replace
import gradle.api.project.ProjectLayout
import gradle.api.project.kotlin
import gradle.api.project.projectScript
import gradle.api.project.sourceSetsToComposeResourcesDirs
import klib.data.type.ansi.Attribute
import klib.data.type.ansi.ansiSpan
import klib.data.type.collections.associateWithNotNull
import klib.data.type.collections.toTreeString
import klib.data.type.pair
import klib.data.type.primitives.string.addPrefixIfNotEmpty
import klib.data.type.primitives.string.case.splitToWords
import klib.data.type.primitives.string.lowercaseFirstChar
import klib.data.type.primitives.string.uppercaseFirstChar
import klib.data.type.tuples.Tuple3
import klib.data.type.tuples.and
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinMetadataTarget

private val KOTLIN_COMPILATIONS = listOf(
    KotlinCompilation.MAIN_COMPILATION_NAME,
    KotlinCompilation.TEST_COMPILATION_NAME,
)

private val ANDROID_TEST_COMPILATIONS = listOf(
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
                    val parts = kotlin.targets.flatMap { target ->
                        target.compilations.map { compilation ->
                            target to compilation.name
                        }
                    }

                    val sourceSets = kotlin.sourceSets.associateWithNotNull { sourceSet ->
                        parts.find { (target, compilationName) ->
                            sourceSet.name == "${target.name}${compilationName.uppercaseFirstChar()}"
                        }?.let { (target, compilationName) ->
                            when (target) {
                                is KotlinMetadataTarget -> Tuple3("src", "", "")
                                is KotlinAndroidTarget ->
                                    (ANDROID_TEST_COMPILATIONS.find { testCompilationName ->
                                        compilationName.startsWith(testCompilationName)
                                    }?.let { testCompilationName ->
                                        "$testCompilationName${
                                            compilationName
                                                .removePrefix(testCompilationName)
                                                .splitToWords()
                                                .joinToString(layout.androidVariantDelimiter)
                                                .addPrefixIfNotEmpty(layout.androidAllVariantsDelimiter)
                                        }"
                                    } ?: compilationName
                                        .splitToWords()
                                        .let { words ->
                                            words.firstOrNull()
                                                .orEmpty() +
                                                words.drop(1)
                                                    .joinToString(layout.androidVariantDelimiter)
                                                    .addPrefixIfNotEmpty(layout.androidAllVariantsDelimiter)
                                        }).pair() and "${layout.targetDelimiter}${target.name}"

                                else -> Tuple3(compilationName, compilationName, "${layout.targetDelimiter}${target.name}")
                            }
                        }
                    }

                    val customSourceSets = (kotlin.sourceSets - sourceSets.keys).associateWithNotNull { sourceSet ->
                        KOTLIN_COMPILATIONS.find { compilationName ->
                            sourceSet.name.endsWith(compilationName.uppercaseFirstChar())
                        }?.let { compilationName ->
                            if (compilationName == KotlinCompilation.MAIN_COMPILATION_NAME)
                                Tuple3("src", "", sourceSet.name.removeSuffix(compilationName.uppercaseFirstChar()))
                            else Tuple3(compilationName, "", sourceSet.name.removeSuffix(compilationName.uppercaseFirstChar()))

                        }
                    }

                    (sourceSets + customSourceSets).forEach { (sourceSet, parts) ->
                        val (srcPart, resourcesPart, targetPart) = parts

                        sourceSet.kotlin.replace(
                            "src/${sourceSet.name}/kotlin",
                            "$srcPart$targetPart",
                        )
                        sourceSet.resources.replace(
                            "src/${sourceSet.name}/resources",
                            "${resourcesPart}Resources$targetPart".lowercaseFirstChar(),
                        )
                        sourceSetsToComposeResourcesDirs[sourceSet] = project.layout.projectDirectory.dir("${resourcesPart}ComposeResources$targetPart".lowercaseFirstChar())
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
