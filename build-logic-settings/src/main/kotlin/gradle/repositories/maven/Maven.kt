package gradle.repositories.maven

import gradle.repositories.RepositoryContentDescriptor
import gradle.repositories.RepositoryPasswordCredentials
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.ArtifactRepositoryContainer

@Serializable
@SerialName("maven")
internal data class Maven(
    override val artifactUrls: Set<String>? = null,
    override val metadataSources: MavenArtifactRepository.MetadataSources? = null,
    override val mavenContent: MavenRepositoryContentDescriptor? = null,
    override val name: String = ArtifactRepositoryContainer.DEFAULT_MAVEN_CENTRAL_REPO_NAME,
    override val content: RepositoryContentDescriptor? = null,
    override val url: String? = null,
    override val allowInsecureProtocol: Boolean? = null,
    override val credentials: RepositoryPasswordCredentials? = null,
) : MavenArtifactRepository
