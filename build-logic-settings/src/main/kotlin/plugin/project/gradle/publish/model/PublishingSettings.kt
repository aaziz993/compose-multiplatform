package plugin.project.gradle.publish.model

import gradle.plugins.publish.Publication
import gradle.plugins.publish.PublicationTransformingSerializer
import gradle.plugins.publish.PublishingExtension
import gradle.plugins.project.EnabledSettings
import gradle.plugins.repository.ArtifactRepository
import gradle.plugins.repository.ArtifactRepositoryTransformingSerializer
import gradle.plugins.repository.ExclusiveContentRepository
import kotlinx.serialization.Serializable

@Serializable
internal data class PublishingSettings(
    override val repositories: List<@Serializable(with = ArtifactRepositoryTransformingSerializer::class) ArtifactRepository>? = null,
    override val exclusiveContent: ExclusiveContentRepository? = null,
    override val publications: List<@Serializable(with = PublicationTransformingSerializer::class) Publication>? = null,
    override val enabled: Boolean = true
) : PublishingExtension, EnabledSettings
