package gradle.plugins.kotlin


import gradle.api.tasks.util.PatternFilterable
import gradle.api.tasks.Task
import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

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
    override fun applyTo(named: T) {
        super<Task>.applyTo(named)

        named as org.jetbrains.kotlin.gradle.tasks.KotlinCompileTool

        super<PatternFilterable>.applyTo(named)

        sources?.let { sources ->
            named.source(*sources.toTypedArray())
        }

        setSources?.let { setSources ->
            named.setSource(*setSources.toTypedArray())
        }

        libraries?.let(named.libraries::setFrom)
        named.destinationDirectory tryAssign destinationDirectory?.let(layout.projectDirectory::dir)
    }

    context(Project)
    override fun applyTo() =
        super<Task>.applyTo(tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompileTool>())
}

@Serializable
@SerialName("KotlinCompileTool")
internal data class KotlinCompileToolImpl(
    override val sources: List<String>? = null,
    override val setSources: List<String>? = null,
    override val libraries: List<String>? = null,
    override val destinationDirectory: String? = null,
    override val includes: Set<String>? = null,
    override val setIncludes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val setExcludes: Set<String>? = null,
    override val dependsOn: SortedSet<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: Set<String>? = null,
    override val finalizedBy: SortedSet<String>? = null,
    override val shouldRunAfter: Set<String>? = null,
    override val name: String = ""
) : KotlinCompileTool
