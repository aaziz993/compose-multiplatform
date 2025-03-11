package gradle.model.repository.maven

import gradle.model.repository.RepositoryContentDescriptor
import gradle.model.repository.RepositoryPasswordCredentials
import gradle.publishing
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.artifacts.ArtifactRepositoryContainer
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

    context(Project)
    override fun applyTo() =
        super.applyTo(
            publishing.repositories.withType<org.gradle.api.artifacts.repositories.MavenArtifactRepository>(),
        ) {
            publishing.repositories.mavenLocal(it)
        }
}
