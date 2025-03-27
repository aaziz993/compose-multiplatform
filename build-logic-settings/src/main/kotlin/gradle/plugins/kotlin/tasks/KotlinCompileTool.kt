package gradle.plugins.kotlin.tasks

import gradle.accessors.files
import gradle.api.tasks.Task
import gradle.api.tasks.applyTo
import gradle.api.tasks.util.PatternFilterable
import gradle.api.tryAssign
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

/**
 * Represents a Kotlin task participating in some stage of the build by compiling sources or running additional Kotlin tools.
 */
internal interface KotlinCompileTool<T : org.jetbrains.kotlin.gradle.tasks.KotlinCompileTool>
    : PatternFilterable<T>, Task<T> {

    /**
     * Adds input sources for this task.
     *
     * @param sources object is evaluated as per [Project.files].
     */
    val sources: Set<String>?

    /**
     * Sets input sources for this task.
     *
     * **Note**: due to [a bug](https://youtrack.jetbrains.com/issue/KT-59632/KotlinCompileTool.setSource-should-replace-existing-sources),
     * the `setSource()` function does not update already added sources.
     *
     * @param sources object is evaluated as per [Project.files].
     */
    val setSources: Set<String>?

    /**
     * Collection of external artifacts participating in the output artifact generation.
     *
     * For example, a Kotlin/JVM compilation task has external JAR files or an
     * external location with already compiled class files.
     */
    val libraries: Set<String>?
    val setLibraries: Set<String>?

    /**
     * The destination directory where the task artifact can be found.
     */
    val destinationDirectory: String?

    context(Project)
    override fun applyTo(receiver: T) {
        super<Task>.applyTo(receiver)
        super<PatternFilterable>.applyTo(receiver)

        receiver::source trySet sources
        receiver::setSource trySet setSources
        receiver.libraries tryFrom libraries
        receiver.libraries trySetFrom setLibraries
        receiver.destinationDirectory tryAssign destinationDirectory?.let(project.layout.projectDirectory::dir)
    }
}

@Serializable
@SerialName("KotlinCompileTool")
internal data class KotlinCompileToolImpl(
    override val sources: Set<String>? = null,
    override val setSources: Set<String>? = null,
    override val libraries: Set<String>? = null,
    override val setLibraries: Set<String>? = null,
    override val destinationDirectory: String? = null,
    override val includes: Set<String>? = null,
    override val setIncludes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val setExcludes: Set<String>? = null,
    override val dependsOn: LinkedHashSet<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: Set<String>? = null,
    override val finalizedBy: LinkedHashSet<String>? = null,
    override val shouldRunAfter: Set<String>? = null,
    override val name: String? = null,
) : KotlinCompileTool<org.jetbrains.kotlin.gradle.tasks.KotlinCompileTool> {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompileTool>())
}
