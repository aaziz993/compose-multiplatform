package gradle.api.publish.maven.tasks

import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.publish.maven.tasks.PublishToMavenLocal
import org.gradle.kotlin.dsl.withType

@Serializable
internal data class PublishToMavenLocal(
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
    override val publication: String? = null,
) : AbstractPublishToMaven<PublishToMavenLocal>() {

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<PublishToMavenLocal>())
}
