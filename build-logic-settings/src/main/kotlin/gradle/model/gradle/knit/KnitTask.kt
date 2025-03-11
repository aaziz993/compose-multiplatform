package gradle.model.gradle.knit

import gradle.trySet
import gradle.model.Task
import gradle.serialization.serializer.AnySerializer
import kotlinx.knit.KnitTask
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Serializable
internal data class KnitTask(
    override val dependsOn: List<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: Map<String, @Serializable(with = AnySerializer::class) Any>? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: List<String>? = null,
    override val finalizedBy: List<String>? = null,
    override val shouldRunAfter: List<String>? = null,
    override val name: String = "",
    val check: Boolean? = null,
    val rootDir: String? = null,
    val files: List<String>? = null,
) : Task {

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as KnitTask

        named::check trySet check
        named::rootDir trySet rootDir?.let(::file)
        named::files trySet files?.let { files(*it.toTypedArray()) }
    }

    context(Project)
    override fun applyTo() =
        super.applyTo(tasks.withType<KnitTask>())
}
