package gradle.model.gradle.publish.repository.maven

import gradle.model.gradle.publish.repository.RepositoryContentDescriptor
import gradle.model.gradle.publish.repository.RepositoryPasswordCredentials
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.ArtifactRepository

@Serializable
@SerialName("maven")
internal data class Maven(
    override val artifactUrls: Set<String>? = null,
    override val metadataSources: MavenArtifactRepository.MetadataSources? = null,
    override val mavenContent: MavenRepositoryContentDescriptor? = null,
    override val name: String? = null,
    override val content: RepositoryContentDescriptor? = null,
    override val url: String? = null,
    override val allowInsecureProtocol: Boolean? = null,
    override val credentials: RepositoryPasswordCredentials? = null,
) : MavenArtifactRepository {

    override fun applyTo(handler: RepositoryHandler) {
        handler.maven {
            super.applyTo(this as ArtifactRepository)
        }
    }
}
