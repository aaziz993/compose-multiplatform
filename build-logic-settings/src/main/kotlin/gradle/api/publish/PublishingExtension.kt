package gradle.api.publish

import gradle.accessors.publishing
import gradle.api.artifacts.dsl.RepositoryHandler
import gradle.api.repositories.ExclusiveContentRepository
import org.gradle.api.Project

/**
 * The configuration of how to “publish" the different components of a project.
 *
 * @since 1.3
 */
internal interface PublishingExtension {

    /**
     * Configures the container of possible repositories to publish to.
     *
     * <pre class='autoTested'>
     * plugins {
     * id 'publishing'
     * }
     *
     * publishing {
     * repositories {
     * // Create an ivy publication destination named “releases”
     * ivy {
     * name = "releases"
     * url = "http://my.org/ivy-repos/releases"
     * }
     * }
     * }
    </pre> *
     *
     * The `repositories` block is backed by a [RepositoryHandler], which is the same DSL as that that is used for declaring repositories to consume dependencies from. However,
     * certain types of repositories that can be created by the repository handler are not valid for publishing, such as [RepositoryHandler.mavenCentral].
     *
     *
     * At this time, only repositories created by the `ivy()` factory method have any effect. Please see [org.gradle.api.publish.ivy.IvyPublication]
     * for information on how this can be used for publishing to Ivy repositories.
     *
     * @param configure The action to configure the container of repositories with.
     */
    val repositories: RepositoryHandler?
    val exclusiveContent: ExclusiveContentRepository?

    /**
     * Configures the publications of this project.
     *
     *
     * The publications container defines the outgoing publications of the project. That is, the consumable representations of things produced
     * by building the project. An example of a publication would be an Ivy Module (i.e. `ivy.xml` and artifacts), or
     * Maven Project (i.e. `pom.xml` and artifacts).
     *
     *
     * Actual publication implementations and the ability to create them are provided by different plugins. The “publishing” plugin itself does not provide any publication types.
     * For example, given that the 'maven-publish' plugin provides a [org.gradle.api.publish.maven.MavenPublication] type, you can create a publication like:
     * <pre class='autoTested'>
     * plugins {
     * id 'maven-publish'
     * }
     *
     * publishing {
     * publications {
     * myPublicationName(MavenPublication) {
     * // Configure the publication here
     * }
     * }
     * }
    </pre> *
     *
     *
     * Please see [org.gradle.api.publish.ivy.IvyPublication] and [org.gradle.api.publish.maven.MavenPublication] for more information on publishing in these specific formats.
     *
     * @param configure The action or closure to configure the publications with.
     */
    val publications: LinkedHashSet<Publication<out org.gradle.api.publish.Publication>>?

    context(Project)
    @Suppress("UNCHECKED_CAST")
    fun applyTo() =
        project.pluginManager.withPlugin("maven-publish") {
            repositories?.applyTo(project.publishing.repositories)

            publications?.forEach { publication ->
                publication.applyTo()
            }
        }
}
