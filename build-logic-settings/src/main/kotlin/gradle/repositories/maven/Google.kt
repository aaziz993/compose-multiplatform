package gradle.repositories.maven

import gradle.plugins.repository.RepositoryContentDescriptor
import gradle.plugins.repository.RepositoryPasswordCredentials
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.ArtifactRepositoryContainer
import org.gradle.api.artifacts.dsl.RepositoryHandler
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
