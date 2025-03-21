package gradle.plugins.dokka.tasks

import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal abstract class DokkaBaseTask<T : org.jetbrains.dokka.gradle.tasks.DokkaBaseTask> : DefaultTask<T>() {

}

@Serializable
@SerialName("DokkaBaseTask")
internal class DokkaBaseTaskImpl(
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
    override val name: String = "",
) : DokkaBaseTask<org.jetbrains.dokka.gradle.tasks.DokkaBaseTask>() {

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<org.jetbrains.dokka.gradle.tasks.DokkaBaseTask>())
}
