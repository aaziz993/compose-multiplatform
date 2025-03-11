package gradle.model.repository.maven

import gradle.model.repository.RepositoryContentDescriptor
import gradle.model.repository.RepositoryPasswordCredentials
import gradle.publishing
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.FlatDirectoryArtifactRepository
import org.gradle.kotlin.dsl.withType

@Serializable
@SerialName("jetbrainsCompose")
internal data class JetbrainsCompose(
    override val artifactUrls: Set<String>? = null,
    override val metadataSources: MavenArtifactRepository.MetadataSources? = null,
    override val mavenContent: MavenRepositoryContentDescriptor? = null,
    override val content: RepositoryContentDescriptor? = null,
    override val allowInsecureProtocol: Boolean? = null,
    override val credentials: RepositoryPasswordCredentials? = null,
) : MavenArtifactRepository {

    override val name: String
        get() = "jetbrainsCompose"

    override val url: String?
        get() = "https://maven.pkg.jetbrains.space/public/p/compose/dev"
}
