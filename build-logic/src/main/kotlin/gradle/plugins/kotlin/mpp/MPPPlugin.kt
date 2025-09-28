package gradle.plugins.kotlin.mpp

import com.github.ajalt.colormath.model.Ansi16
import gradle.api.file.replace
import gradle.api.project.ProjectLayout
import gradle.api.project.kotlin
import gradle.api.project.projectScript
import gradle.api.project.sourceSetsToComposeResourcesDirs
import klib.data.type.collections.associateWithNotNull
import klib.data.type.collections.toTreeString
import klib.data.type.pair
import klib.data.type.primitives.string.ansi.Attribute
import klib.data.type.primitives.string.ansi.ansiSpan
import klib.data.type.primitives.string.lowercaseFirstChar
import klib.data.type.primitives.string.uppercaseFirstChar
import klib.data.type.tuples.and
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinMetadataTarget

private val KOTLIN_COMPILATIONS = listOf(
    KotlinCompilation.MAIN_COMPILATION_NAME,
    KotlinCompilation.TEST_COMPILATION_NAME,
)

private val ANDROID_APPLICATION_COMPILATIONS = KOTLIN_COMPILATIONS + listOf(
    "unitTest",
    "instrumentedTest",
)

public class MPPPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
                adjustSourceSets()
                registerSourcesHierarchyTask()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun Project.adjustSourceSets() {
        kotlin {
            when (val layout = projectScript.layout) {
                is ProjectLayout.Flat -> {
                    val sourceNameParts = kotlin.targets.flatMap { target ->
                        target.compilations.map { compilation ->
                            target to compilation.name
                        }
                    } + kotlin.targets.filterIsInstance<KotlinMetadataTarget>().map { target ->
                        target to KotlinCompilation.TEST_COMPILATION_NAME
                    } + kotlin.targets.filterIsInstance<KotlinAndroidTarget>().flatMap { target ->
                        ANDROID_APPLICATION_COMPILATIONS.map { compilationName -> target to compilationName }
                    }

                    val sourceSets = kotlin.sourceSets.associateWithNotNull { sourceSet ->
                        sourceNameParts.find { (target, compilationName) ->
                            sourceSet.name == "${
                                if (target is KotlinMetadataTarget) "common" else target.targetName
                            }${compilationName.uppercaseFirstChar()}"
                        }?.let { (target, compilationName) ->
                            (if (compilationName == KotlinCompilation.MAIN_COMPILATION_NAME) "src" to ""
                            else compilationName.pair()) and if (target is KotlinMetadataTarget) ""
                            else "${layout.targetDelimiter}${target.targetName}"
                        }
                    }

                    val customSourceSets = (kotlin.sourceSets - sourceSets.keys).associateWithNotNull { sourceSet ->
                        KOTLIN_COMPILATIONS.find { compilationName ->
                            sourceSet.name.endsWith(compilationName.uppercaseFirstChar())
                        }?.let { compilationName ->
                            (if (compilationName == KotlinCompilation.MAIN_COMPILATION_NAME) "src" to ""
                            else compilationName.pair()) and "${layout.targetDelimiter}${
                                sourceSet.name.removeSuffix(compilationName.uppercaseFirstChar())
                            }"
                        }
                    }

                    (sourceSets + customSourceSets).forEach { (sourceSet, parts) ->
                        val (srcPart, resourcesPart, targetPart) = parts

                        sourceSet.kotlin.replace("src/${sourceSet.name}/kotlin", "$srcPart$targetPart")
                        sourceSet.resources.replace(
                            "src/${sourceSet.name}/resources",
                            "${resourcesPart}Resources$targetPart".lowercaseFirstChar(),
                        )
                        sourceSetsToComposeResourcesDirs[sourceSet] = project.layout.projectDirectory.dir(
                            "${resourcesPart}ComposeResources$targetPart".lowercaseFirstChar(),
                        )
                    }
                }

                else -> Unit
            }
        }
    }

    private fun Project.registerSourcesHierarchyTask() =
        tasks.register("sourcesHierarchy") {
            group = "help"
            description = "Prints Kotlin source set hierarchy"

            doLast {
                val dependees = kotlin.sourceSets
                    .associate { sourceSet -> sourceSet.name to mutableListOf<String>() }
                    .toMutableMap()

                kotlin.sourceSets.forEach { sourceSet ->
                    sourceSet.dependsOn.forEach { parentSourceSet ->
                        dependees[parentSourceSet.name]?.add(sourceSet.name)
                    }
                }

                val commonMain = KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME
                project.logger.lifecycle(
                    "${
                        project.toString()
                            .uppercase()
                            .ansiSpan {
                                attribute(Attribute.INTENSITY_BOLD)
                                attribute(Ansi16(36))
                            }
                    } Kotlin SourceSet Hierarchy from commonMain:",
                )
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
                    project.logger.lifecycle(
                        "${
                            project.toString()
                                .uppercase()
                                .ansiSpan {
                                    attribute(Attribute.INTENSITY_BOLD)
                                    attribute(Ansi16(36))
                                }
                        } Kotlin SourceSet Hierarchy from $root:",
                    )
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
