package gradle.api.repositories.ivy

import gradle.api.applyTo
import gradle.api.repositories.ArtifactRepository
import gradle.api.repositories.AuthenticationSupported
import gradle.api.repositories.PasswordCredentials
import gradle.api.repositories.RepositoryContentDescriptorImpl
import gradle.api.repositories.UrlArtifactRepository
import klib.data.type.reflection.trySet
import gradle.reflection.tryApply
import klib.data.type.reflection.tryApply
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.IvyArtifactRepository
import org.gradle.api.file.Directory
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.withType

/**
 * An artifact repository which uses an Ivy format to store artifacts and meta-data.
 *
 *
 * When used to resolve metadata and artifact files, all available patterns will be searched.
 *
 *
 * When used to upload metadata and artifact files, only a single, primary pattern will be used:
 *
 *  1. If a URL is specified via [.setUrl] then that URL will be used for upload, combined with the applied [.layout].
 *  1. If no URL has been specified but additional patterns have been added via [.artifactPattern] or [.ivyPattern], then the first defined pattern will be used.
 *
 *
 *
 * Repositories of this type are created by the [RepositoryHandler.ivy] group of methods.
 */
@Serializable
@SerialName("ivy")
internal data class IvyArtifactRepository(
    override val name: String = "ivy${Math.random() * Int.MAX_VALUE}",
    override val content: RepositoryContentDescriptorImpl? = null,
    override val url: String? = null,
    override val allowInsecureProtocol: Boolean? = null,
    override val credentials: PasswordCredentials? = null,
    /**
     * Adds an independent pattern that will be used to locate artifact files in this repository. This pattern will be used to locate ivy files as well, unless a specific
     * ivy pattern is supplied via [.ivyPattern].
     *
     * If this pattern is not a fully-qualified URL, it will be interpreted as a file relative to the project directory.
     * It is not interpreted relative the URL specified in [.setUrl].
     *
     * Patterns added in this way will be in addition to any layout-based patterns added via [.setUrl].
     *
     * @param pattern The artifact pattern.
     */
    val artifactPatterns: Set<String>? = null,
    /**
     * Adds an independent pattern that will be used to locate ivy files in this repository.
     *
     * If this pattern is not a fully-qualified URL, it will be interpreted as a file relative to the project directory.
     * It is not interpreted relative the URL specified in [.setUrl].
     *
     * Patterns added in this way will be in addition to any layout-based patterns added via [.setUrl].
     *
     * @param pattern The ivy pattern.
     */
    val ivyPatterns: Set<String>? = null,
    /**
     * Specifies how the items of the repository are organized.
     *
     *
     * Recognised values are as follows:
     *
     * <h4>'gradle'</h4>
     *
     *
     * A Repository Layout that applies the following patterns:
     *
     *
     *  * Artifacts: `$baseUri/{@value #GRADLE_ARTIFACT_PATTERN}`
     *  * Ivy: `$baseUri/{@value #GRADLE_IVY_PATTERN}`
     *
     * <h4>'maven'</h4>
     *
     *
     * A Repository Layout that applies the following patterns:
     *
     *
     *  * Artifacts: `$baseUri/{@value #MAVEN_ARTIFACT_PATTERN}`
     *  * Ivy: `$baseUri/{@value #MAVEN_IVY_PATTERN}`
     *
     *
     *
     * Following the Maven convention, the 'organisation' value is further processed by replacing '.' with '/'.
     *
     * <h4>'ivy'</h4>
     *
     *
     * A Repository Layout that applies the following patterns:
     *
     *
     *  * Artifacts: `$baseUri/{@value #IVY_ARTIFACT_PATTERN}`
     *  * Ivy: `$baseUri/{@value #IVY_ARTIFACT_PATTERN}`
     *
     *
     * @param layoutName The name of the layout to use.
     * @see .patternLayout
     */
    val layout: String? = null,
    /**
     * Specifies how the items of the repository are organized.
     *
     *
     * The layout is configured with the supplied closure.
     * <pre class='autoTested'>
     * repositories {
     * ivy {
     * patternLayout {
     * artifact '[module]/[revision]/[artifact](.[ext])'
     * ivy '[module]/[revision]/ivy.xml'
     * }
     * }
     * }
    </pre> *
     *
     * The available pattern tokens are listed as part of [Ivy's Main Concepts documentation](http://ant.apache.org/ivy/history/master/concept.html#patterns).
     *
     * @param config The action used to configure the layout.
     * @since 5.0
     */
    val patternLayout: IvyPatternRepositoryLayout? = null,

    /**
     * Returns the meta-data provider used when resolving artifacts from this repository. The provider is responsible for locating and interpreting the meta-data
     * for the modules and artifacts contained in this repository. Using this provider, you can fine tune how this resolution happens.
     *
     * @return The meta-data provider for this repository.
     */
    val resolve: IvyArtifactRepositoryMetaDataProvider? = null,
    /**
     * Configures the metadata sources for this repository. This method will replace any previously configured sources
     * of metadata.
     *
     * @param configureAction the configuration of metadata sources.
     *
     * @since 4.5
     */
    val metadataSources: MetadataSources? = null,
) : ArtifactRepository<IvyArtifactRepository>, UrlArtifactRepository<IvyArtifactRepository>, AuthenticationSupported<IvyArtifactRepository> {

    context(Settings)
    override fun applyTo(receiver: IvyArtifactRepository) =
        super<ArtifactRepository>.applyTo(receiver)

    context(Project)
    override fun applyTo(receiver: IvyArtifactRepository) =
        super<ArtifactRepository>.applyTo(receiver)

    context(Settings)
    override fun applyTo(receiver: RepositoryHandler) =
        applyTo(receiver.withType<IvyArtifactRepository>()) { _name, action ->
            receiver.ivy {
                name = _name
                action.execute(this)
            }
        }

    context(Project)
    override fun applyTo(receiver: RepositoryHandler) =
        applyTo(receiver.withType<IvyArtifactRepository>()) { _name, action ->
            receiver.ivy {
                name = _name
                action.execute(this)
            }
        }

    context(Directory)
    override fun _applyTo(receiver: IvyArtifactRepository) {
        super<ArtifactRepository>._applyTo(receiver)
        super<UrlArtifactRepository>._applyTo(receiver)
        super<AuthenticationSupported>._applyTo(receiver)

        artifactPatterns?.forEach(receiver::artifactPattern)
        ivyPatterns?.forEach(receiver::ivyPattern)
        receiver::layout trySet layout
        receiver::patternLayout tryApply patternLayout?.let { patternLayout -> patternLayout::applyTo }
        resolve?.applyTo(receiver.resolve)
        receiver::metadataSources tryApply metadataSources?.let { metadataSources -> metadataSources::applyTo }
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
         * Indicates that this repository will contain Ivy descriptors.
         * If the Ivy file contains a marker telling that Gradle metadata exists
         * for this component, Gradle will *also* look for the Gradle metadata
         * file. Gradle module metadata redirection will not happen if `ignoreGradleMetadataRedirection()` has been used.
         */
        val ivyDescriptor: Boolean? = null,
        /**
         * Indicates that this repository may not contain metadata files,
         * but we can infer it from the presence of an artifact file.
         */
        val artifact: Boolean? = null,
        /**
         * Indicates that this repository will ignore Gradle module metadata redirection markers found in Ivy files.
         *
         * @since 5.6
         */
        val ignoreGradleMetadataRedirection: Boolean? = null,
    ) {

        fun applyTo(receiver: IvyArtifactRepository.MetadataSources) {
            receiver::gradleMetadata trySet gradleMetadata
            receiver::ivyDescriptor trySet ivyDescriptor
            receiver::artifact trySet artifact
            receiver::ignoreGradleMetadataRedirection trySet ignoreGradleMetadataRedirection
        }
    }
}


