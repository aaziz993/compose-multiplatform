package gradle.api.publish.maven.tasks

import gradle.accessors.publishing
import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.withType

/**
 * Base class for tasks that publish a [org.gradle.api.publish.maven.MavenPublication].
 *
 * @since 2.4
 */
internal abstract class AbstractPublishToMaven<T : org.gradle.api.publish.maven.tasks.AbstractPublishToMaven> : DefaultTask<T>() {

    abstract val publication: String?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        publication?.let(publishing.publications::getByName)?.let { publication ->
            receiver.setPublication(publication as MavenPublication)
        }
    }
}

@Serializable
@SerialName("AbstractPublishToMaven")
internal data class AbstractPublishToMavenImpl(
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
    override val publication: String? = null,
) : AbstractPublishToMaven<org.gradle.api.publish.maven.tasks.AbstractPublishToMaven>() {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.gradle.api.publish.maven.tasks.AbstractPublishToMaven>())
}
