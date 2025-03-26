package gradle.plugins.publish.model

import gradle.api.artifacts.dsl.RepositoryHandler
import gradle.api.publish.Publication
import gradle.api.publish.PublicationKeyTransformingSerializer
import gradle.api.publish.PublishingExtension
import gradle.api.repositories.ExclusiveContentRepository
import gradle.plugins.project.EnabledSettings
import kotlinx.serialization.Serializable

@Serializable
internal data class PublishingSettings(
    override val repositories: RepositoryHandler? = null,
    override val exclusiveContent: ExclusiveContentRepository? = null,
    override val publications: LinkedHashSet<@Serializable(with = PublicationKeyTransformingSerializer::class) Publication<*>>? = null,
    override val enabled: Boolean = true
) : PublishingExtension, EnabledSettings
