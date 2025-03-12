package gradle.repositories.maven

import gradle.repositories.RepositoryContentDescriptor
import gradle.repositories.RepositoryPasswordCredentials
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.ArtifactRepositoryContainer
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.withType

@Serializable
@SerialName("mavenLocal")
internal data class MavenLocal(
    override val artifactUrls: Set<String>? = null,
    override val metadataSources: MavenArtifactRepository.MetadataSources? = null,
    override val mavenContent: MavenRepositoryContentDescriptor? = null,
    override val name: String = ArtifactRepositoryContainer.DEFAULT_MAVEN_LOCAL_REPO_NAME,
    override val content: RepositoryContentDescriptor? = null,
    override val allowInsecureProtocol: Boolean? = null,
    override val credentials: RepositoryPasswordCredentials? = null,
) : MavenArtifactRepository {

    override val url: String?
        get() = null

    override fun applyTo(handler: RepositoryHandler) =
        super.applyTo(handler.withType<org.gradle.api.artifacts.repositories.MavenArtifactRepository>()) {
            handler.mavenLocal(it)
        }
}
