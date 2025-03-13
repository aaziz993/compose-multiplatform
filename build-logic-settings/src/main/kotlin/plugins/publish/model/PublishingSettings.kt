package plugins.publish.model

import gradle.project.EnabledSettings
import gradle.api.publish.Publication
import gradle.api.publish.PublicationTransformingSerializer
import gradle.api.publish.PublishingExtension
import gradle.api.repositories.ArtifactRepository
import gradle.api.repositories.ArtifactRepositoryTransformingSerializer
import gradle.api.repositories.ExclusiveContentRepository
import kotlinx.serialization.Serializable

@Serializable
internal data class PublishingSettings(
    override val repositories: List<@Serializable(with = ArtifactRepositoryTransformingSerializer::class) ArtifactRepository>? = null,
    override val exclusiveContent: ExclusiveContentRepository? = null,
    override val publications: List<@Serializable(with = PublicationTransformingSerializer::class) Publication>? = null,
    override val enabled: Boolean = true
) : PublishingExtension, EnabledSettings
