package gradle.plugins.publish

import gradle.serialization.serializer.AnySerializer
import gradle.tasks.Task
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

/**
 * Base class for tasks that publish a [org.gradle.api.publish.maven.MavenPublication].
 *
 * @since 2.4
 */
internal abstract class AbstractPublishToMaven : Task{
    context(Project)
    override fun applyTo() =
        super.applyTo(tasks.withType<org.gradle.api.publish.maven.tasks.AbstractPublishToMaven>())
}

@Serializable
@SerialName("AbstractPublishToMaven")
internal data class AbstractPublishToMavenImpl(
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
) : AbstractPublishToMaven()
