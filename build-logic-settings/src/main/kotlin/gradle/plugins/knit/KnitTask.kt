package gradle.plugins.knit


import gradle.api.tasks.Task
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import kotlinx.knit.KnitTask
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
internal data class KnitTask(
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
    override val name: String = "",
    val check: Boolean? = null,
    val rootDir: String? = null,
    val files: List<String>? = null,
) : Task {

        context(Project)
    override fun applyTo(named: T) {
        super.applyTo(named)

        named as KnitTask

        named::check trySet check
        named::rootDir trySet rootDir?.let(::file)
        named::files trySet files?.let { files(*it.toTypedArray()) }
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<KnitTask>())
}
