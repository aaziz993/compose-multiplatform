package gradle.api.repositories.maven

import gradle.api.applyTo
import gradle.api.repositories.ArtifactRepository
import gradle.api.repositories.AuthenticationSupported
import gradle.api.repositories.UrlArtifactRepository
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.withType

/**
 * An artifact repository which uses a Maven format to store artifacts and meta-data.
 *
 *
 * Repositories of this type are created by the [RepositoryHandler.maven] group of methods.
 */
internal interface MavenArtifactRepository
    : ArtifactRepository<MavenArtifactRepository>, UrlArtifactRepository, AuthenticationSupported {

    /**
     * Sets the additional URLs to use to find artifact files. Note that these URLs are not used to find POM files.
     *
     * @param urls The URLs.
     * @since 4.0
     */
    val artifactUrls: Set<String>?

    /**
     * Configures the metadata sources for this repository. This method will replace any previously configured sources
     * of metadata.
     *
     * @param configureAction the configuration of metadata sources.
     *
     * @since 4.5
     */
    val metadataSources: MetadataSources?

    /**
     * Configures the content of this Maven repository.
     * @param configureAction the configuration action
     *
     * @since 5.1
     */
    val mavenContent: MavenRepositoryContentDescriptor?

    context(Settings)
    override fun applyTo(named: MavenArtifactRepository) =
        super<ArtifactRepository>.applyTo(named)

    context(Project)
    override fun applyTo(named: MavenArtifactRepository) =
        super<ArtifactRepository>.applyTo(named)

    override fun _applyTo(named: MavenArtifactRepository) {
        super<ArtifactRepository>._applyTo(named)

        super<UrlArtifactRepository>.applyTo(named)
        super<AuthenticationSupported>.applyTo(named)

        artifactUrls?.let { artifactUrls ->
            named.artifactUrls(*artifactUrls.toTypedArray())
        }

        metadataSources?.applyTo(named.metadataSources)

        mavenContent?.let { mavenContent ->
            named.mavenContent(mavenContent::applyTo)
        }
    }

    context(Settings)
    override fun applyTo(handler: RepositoryHandler) =
        applyTo(handler.withType<MavenArtifactRepository>()) { _name, action ->
            handler.maven {
                name = _name
                action.execute(this)
            }
        }

    context(Project)
    override fun applyTo(handler: RepositoryHandler) =
        applyTo(handler.withType<MavenArtifactRepository>()) { _name, action ->
            handler.maven {
                name = _name
                action.execute(this)
            }
        }

    /**
     * Allows configuring the sources of metadata for a specific repository.
     *
     * @since 4.5
     */
    @Serializable
    data class MetadataSources(
        /**
         * Indicates that this repository will contain Gradle metadata.
         */
        val gradleMetadata: Boolean? = null,
        /**
         * Indicates that this repository will contain Maven POM files.
         * If the POM file contains a marker telling that Gradle metadata exists
         * for this component, Gradle will *also* look for the Gradle metadata
         * file. Gradle module metadata redirection will not happen if `ignoreGradleMetadataRedirection()` has been used.
         */
        val mavenPom: Boolean? = null,
        /**
         * Indicates that this repository may not contain metadata files,
         * but we can infer it from the presence of an artifact file.
         */
        val artifact: Boolean? = null,
        /**
         * Indicates that this repository will ignore Gradle module metadata redirection markers found in POM files.
         *
         * @since 5.6
         */
        val ignoreGradleMetadataRedirection: Boolean? = null,
    ) {

        fun applyTo(sources: MavenArtifactRepository.MetadataSources) {
            gradleMetadata?.takeIf { it }?.run { sources.gradleMetadata() }
            mavenPom?.takeIf { it }?.run { sources.mavenPom() }
            artifact?.takeIf { it }?.run { sources.artifact() }
            ignoreGradleMetadataRedirection?.takeIf { it }?.run { sources.ignoreGradleMetadataRedirection() }
        }
    }
}
