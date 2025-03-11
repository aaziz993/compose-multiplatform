package gradle.model.repository.maven

import gradle.model.repository.RepositoryContentDescriptor
import gradle.model.repository.RepositoryPasswordCredentials
import gradle.publishing
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.artifacts.ArtifactRepositoryContainer
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.withType

@Serializable
@SerialName("mavenCentral")
internal data class MavenCentral(
    override val artifactUrls: Set<String>? = null,
    override val metadataSources: MavenArtifactRepository.MetadataSources? = null,
    override val mavenContent: MavenRepositoryContentDescriptor? = null,
    override val content: RepositoryContentDescriptor? = null,
    override val allowInsecureProtocol: Boolean? = null,
    override val credentials: RepositoryPasswordCredentials? = null,
) : MavenArtifactRepository {

    override val name: String
        get() = ArtifactRepositoryContainer.DEFAULT_MAVEN_CENTRAL_REPO_NAME

    override val url: String?
        get() = ArtifactRepositoryContainer.MAVEN_CENTRAL_URL

    override fun applyTo(handler: RepositoryHandler) =
        super.applyTo(handler.withType<org.gradle.api.artifacts.repositories.MavenArtifactRepository>()) {
            handler.mavenCentral(it)
        }
}
