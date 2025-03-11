package gradle.model.repository.maven

import gradle.model.repository.RepositoryContentDescriptor
import gradle.model.repository.RepositoryPasswordCredentials
import gradle.publishing
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.artifacts.ArtifactRepositoryContainer
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.FlatDirectoryArtifactRepository
import org.gradle.kotlin.dsl.withType

@Serializable
@SerialName("google")
internal data class Google(
    override val artifactUrls: Set<String>? = null,
    override val metadataSources: MavenArtifactRepository.MetadataSources? = null,
    override val mavenContent: MavenRepositoryContentDescriptor? = null,
    override val content: RepositoryContentDescriptor? = null,
    override val allowInsecureProtocol: Boolean? = null,
    override val credentials: RepositoryPasswordCredentials? = null,
) : MavenArtifactRepository {

    override val name: String
        get() = "google"

    override val url: String?
        get() = ArtifactRepositoryContainer.GOOGLE_URL

    override fun applyTo(handler: RepositoryHandler) =
        super.applyTo(handler.withType<org.gradle.api.artifacts.repositories.MavenArtifactRepository>()) {
            handler.google(it)
        }
}
