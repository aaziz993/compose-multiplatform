package gradle.plugins.kotlin.tasks

import gradle.api.tasks.util.PatternFilterable
import gradle.api.tasks.Task
import gradle.api.tryAssign
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompileTool

/**
 * Represents a Kotlin task participating in some stage of the build by compiling sources or running additional Kotlin tools.
 */
internal interface KotlinCompileTool : PatternFilterable, Task {

    /**
     * Adds input sources for this task.
     *
     * @param sources object is evaluated as per [org.gradle.api.Project.files].
     */
    val sources: List<String>?

    /**
     * Sets input sources for this task.
     *
     * **Note**: due to [a bug](https://youtrack.jetbrains.com/issue/KT-59632/KotlinCompileTool.setSource-should-replace-existing-sources),
     * the `setSource()` function does not update already added sources.
     *
     * @param sources object is evaluated as per [org.gradle.api.Project.files].
     */
    val setSources: List<String>?

    /**
     * Collection of external artifacts participating in the output artifact generation.
     *
     * For example, a Kotlin/JVM compilation task has external JAR files or an
     * external location with already compiled class files.
     */
    val libraries: List<String>?

    /**
     * The destination directory where the task artifact can be found.
     */
    val destinationDirectory: String?

    context(Project)
    fun applyTo(recipient: KotlinCompileTool) {
        sources?.let { sources ->
            tool.source(* sources.toTypedArray())
        }

        setSources?.let { setSources ->
            tool.setSource(* setSources.toTypedArray())
        }

        libraries?.let(tool.libraries::setFrom)
        tool.destinationDirectory tryAssign destinationDirectory?.let(::file)
    }
}
