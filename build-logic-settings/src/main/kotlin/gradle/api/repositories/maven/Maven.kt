package gradle.api.repositories.maven

import gradle.api.repositories.RepositoryContentDescriptorImpl
import gradle.api.repositories.PasswordCredentials
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.ArtifactRepositoryContainer

@Serializable
@SerialName("maven")
internal data class Maven(
    override val artifactUrls: Set<String>? = null,
    override val metadataSources: MavenArtifactRepository.MetadataSources? = null,
    override val mavenContent: MavenRepositoryContentDescriptor? = null,
    override val name: String = "${ArtifactRepositoryContainer.DEFAULT_MAVEN_CENTRAL_REPO_NAME}${Math.random() * Int.MAX_VALUE}",
    override val content: RepositoryContentDescriptorImpl? = null,
    override val url: String? = null,
    override val allowInsecureProtocol: Boolean? = null,
    override val credentials: PasswordCredentials? = null,
) : MavenArtifactRepository
