package gradle.model.gradle.publish

import gradle.model.Task
import gradle.serialization.serializer.AnySerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Base class for tasks that publish a [org.gradle.api.publish.maven.MavenPublication].
 *
 * @since 2.4
 */
@Serializable
internal abstract class AbstractPublishToMaven : Task

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
) : AbstractPublishToMaven() {

    override val name: String
        get() = ""
}
