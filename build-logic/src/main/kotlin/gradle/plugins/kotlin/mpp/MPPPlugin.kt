package gradle.plugins.kotlin.mpp

import com.github.ajalt.colormath.model.Ansi16
import gradle.api.project.kotlin
import klib.data.type.collections.toTreeString
import klib.data.type.primitives.string.ansi.Attribute
import klib.data.type.primitives.string.ansi.ansiSpan
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

public class MPPPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
                adjustSourceSets()
                registerTargetsHierarchyTask()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun Project.adjustSourceSets() = kotlin.flatten()

    private fun Project.registerTargetsHierarchyTask() =
        tasks.register("targetsHierarchy") {
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
