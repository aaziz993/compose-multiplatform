package gradle.plugins.dokka

import gradle.collection.SerializableAnyMap
import gradle.api.tasks.Task
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal abstract class DokkaBaseTask : Task {

    context(Project)
    override fun applyTo() =
        super.applyTo(tasks.withType<org.jetbrains.dokka.gradle.tasks.DokkaBaseTask>())
}

@Serializable
@SerialName("DokkaBaseTask")
internal class DokkaBaseTaskImpl(
    override val dependsOn: List<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: List<String>? = null,
    override val finalizedBy: List<String>? = null,
    override val shouldRunAfter: List<String>? = null,
    override val name: String = "",
) : DokkaBaseTask()
