package plugins.publish.model

import gradle.plugins.project.EnabledSettings
import gradle.publish.Publication
import gradle.publish.PublicationTransformingSerializer
import gradle.publish.PublishingExtension
import gradle.repositories.ArtifactRepository
import gradle.repositories.ArtifactRepositoryTransformingSerializer
import gradle.repositories.ExclusiveContentRepository
import kotlinx.serialization.Serializable

@Serializable
internal data class PublishingSettings(
    override val repositories: List<@Serializable(with = ArtifactRepositoryTransformingSerializer::class) ArtifactRepository>? = null,
    override val exclusiveContent: ExclusiveContentRepository? = null,
    override val publications: List<@Serializable(with = PublicationTransformingSerializer::class) Publication>? = null,
    override val enabled: Boolean = true
) : PublishingExtension, EnabledSettings
