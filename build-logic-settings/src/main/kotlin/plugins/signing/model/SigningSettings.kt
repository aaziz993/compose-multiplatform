package plugins.signing.model

import gradle.project.EnabledSettings
import gradle.api.publish.Publication
import gradle.api.publish.PublicationTransformingSerializer
import gradle.api.publish.PublishingExtension
import gradle.api.repositories.ArtifactRepository
import gradle.api.repositories.ArtifactRepositoryTransformingSerializer
import gradle.api.repositories.ExclusiveContentRepository
import gradle.plugins.signing.ClassifierFile
import gradle.plugins.signing.InMemoryPgpKeys
import gradle.plugins.signing.SigningExtension
import kotlinx.serialization.Serializable

@Serializable
internal data class SigningSettings(
    override val required: Boolean? = null,
    override val useGpgCmd: Boolean? = null,
    override val useInMemoryPgpKeys: InMemoryPgpKeys? = null,
    override val signTasks: List<String>? = null,
    override val signConfigurations: List<String>? = null,
    override val signPublications: List<String>? = null,
    override val signPublishArtifacts: List<String>? = null,
    override val signFiles: List<String>? = null,
    override val signClassifierFiles: List<ClassifierFile>? = null,
    override val enabled: Boolean = true,
    val generateGpg: GenerateGgp? = null,
) : SigningExtension(), EnabledSettings
