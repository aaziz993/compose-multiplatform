package plugin.project.gradle.publish.model

import gradle.model.gradle.publish.Publication
import gradle.model.gradle.publish.PublicationTransformingSerializer
import gradle.model.gradle.publish.PublishingExtension
import gradle.model.project.EnabledSettings
import gradle.model.repository.ArtifactRepository
import gradle.model.repository.ArtifactRepositoryTransformingSerializer
import gradle.model.repository.ExclusiveContentRepository
import kotlinx.serialization.Serializable

@Serializable
internal data class PublishingSettings(
    override val repositories: List<@Serializable(with = ArtifactRepositoryTransformingSerializer::class) ArtifactRepository>? = null,
    override val exclusiveContent: ExclusiveContentRepository? = null,
    override val publications: List<@Serializable(with = PublicationTransformingSerializer::class) Publication>? = null,
    override val enabled: Boolean = true
) : PublishingExtension, EnabledSettings
