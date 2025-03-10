package gradle.model.repository.maven

import gradle.model.gradle.publish.repository.RepositoryContentDescriptor
import gradle.model.gradle.publish.repository.RepositoryPasswordCredentials
import gradle.publishing
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.ArtifactRepository
import org.gradle.kotlin.dsl.withType

@Serializable
@SerialName("mavenCentral")
internal data class MavenCentral(
    override val artifactUrls: Set<String>? = null,
    override val metadataSources: MavenArtifactRepository.MetadataSources? = null,
    override val mavenContent: MavenRepositoryContentDescriptor? = null,
    override val name: String="mavenCentral",
    override val content: RepositoryContentDescriptor? = null,
    override val url: String? = null,
    override val allowInsecureProtocol: Boolean? = null,
    override val credentials: RepositoryPasswordCredentials? = null,
) : MavenArtifactRepository {

    context(Project)
    override fun applyTo() =
        super.applyTo(
            publishing.repositories.withType<org.gradle.api.artifacts.repositories.MavenArtifactRepository>(),
        ) {
            publishing.repositories.mavenCentral(it)
        }
}
