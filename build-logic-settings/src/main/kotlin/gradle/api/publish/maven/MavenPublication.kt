package gradle.api.publish.maven

import gradle.accessors.publishing
import gradle.api.applyTo
import gradle.api.publish.Publication
import klib.data.type.reflection.trySet
import klib.data.type.reflection.tryApply
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenArtifact
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.withType

/**
 * A `MavenPublication` is the representation/configuration of how Gradle should publish something in Maven format.
 *
 * You directly add a named Maven publication the project's `publishing.publications` container by providing [MavenPublication] as the type.
 * <pre>
 * publishing {
 * publications {
 * myPublicationName(MavenPublication) {
 * // Configure the publication here
 * }
 * }
 * }
</pre> *
 *
 * The default Maven POM identifying attributes are mapped as follows:
 *
 *  * `groupId` - `project.group`
 *  * `artifactId` - `project.name`
 *  * `version` - `project.version`
 *
 *
 *
 *
 * For certain common use cases, it's often sufficient to specify the component to publish, and nothing more ([.from].
 * The published component is used to determine which artifacts to publish, and which dependencies should be listed in the generated POM file.
 *
 *
 * To add additional artifacts to the set published, use the [.artifact] and [.artifact] methods.
 * You can also completely replace the set of published artifacts using [.setArtifacts].
 * Together, these methods give you full control over what artifacts will be published.
 *
 *
 * To customize the metadata published in the generated POM, set properties, e.g. [MavenPom.getDescription], on the POM returned via the [.getPom]
 * method or directly by an action (or closure) passed into [.pom].
 * As a last resort, it is possible to modify the generated POM using the [MavenPom.withXml] method.
 *
 *
 * <pre class='autoTested'>
 * // Example of publishing a Java module with a source artifact and a customized POM
 * plugins {
 * id 'java'
 * id 'maven-publish'
 * }
 *
 * task sourceJar(type: Jar) {
 * from sourceSets.main.allJava
 * archiveClassifier = "sources"
 * }
 *
 * publishing {
 * publications {
 * myPublication(MavenPublication) {
 * from components.java
 * artifact sourceJar
 * pom {
 * name = "Demo"
 * description = "A demonstration of Maven POM customization"
 * url = "http://www.example.com/project"
 * licenses {
 * license {
 * name = "The Apache License, Version 2.0"
 * url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
 * }
 * }
 * developers {
 * developer {
 * id = "johnd"
 * name = "John Doe"
 * email = "john.doe@example.com"
 * }
 * }
 * scm {
 * connection = "scm:svn:http://subversion.example.com/svn/project/trunk/"
 * developerConnection = "scm:svn:https://subversion.example.com/svn/project/trunk/"
 * url = "http://subversion.example.com/svn/project/trunk/"
 * }
 * }
 * }
 * }
 * }
</pre> *
 *
 * @since 1.4
 */
@Serializable
@SerialName("maven")
internal data class MavenPublication(
    override val withoutBuildIdentifier: Boolean? = null,
    override val withBuildIdentifier: Boolean? = null,
    override val name: String = "maven",
    /**
     * Configures the POM that will be published.
     *
     * The supplied action will be executed against the [.getPom] result. This method also accepts a closure argument, by type coercion.
     *
     * @param configure The configuration action.
     */
    val pom: MavenPom? = null,
    /**
     * Provides the software component that should be published.
     *
     *
     *  * Any artifacts declared by the component will be included in the publication.
     *  * The dependencies declared by the component will be included in the published meta-data.
     *
     *
     * Currently 3 types of component are supported: 'components.java' (added by the JavaPlugin), 'components.web' (added by the WarPlugin)
     * and `components.javaPlatform` (added by the JavaPlatformPlugin).
     *
     * For any individual MavenPublication, only a single component can be provided in this way.
     *
     * The following example demonstrates how to publish the 'java' component to a Maven repository.
     * <pre class='autoTested'>
     * plugins {
     * id 'java'
     * id 'maven-publish'
     * }
     *
     * publishing {
     * publications {
     * maven(MavenPublication) {
     * from components.java
     * }
     * }
     * }
    </pre> *
     *
     * @param component The software component to publish.
     */
    val from: String? = null,
    /**
     * Creates an [MavenArtifact] to be included in the publication, which is configured by the associated action.
     *
     * The first parameter is used to create a custom artifact and add it to the publication, as per [.artifact].
     * The created [MavenArtifact] is then configured using the supplied action, which can override the extension or classifier of the artifact.
     * This method also accepts the configure action as a closure argument, by type coercion.
     *
     * <pre class='autoTested'>
     * plugins {
     * id 'maven-publish'
     * }
     *
     * task sourceJar(type: Jar) {
     * archiveClassifier = "sources"
     * }
     *
     * publishing {
     * publications {
     * maven(MavenPublication) {
     * artifact(sourceJar) {
     * // These values will be used instead of the values from the task. The task values will not be updated.
     * classifier = "src"
     * extension = "zip"
     * }
     * artifact("my-docs-file.htm") {
     * classifier = "documentation"
     * extension = "html"
     * }
     * }
     * }
     * }
    </pre> *
     *
     * @param source The source of the artifact.
     * @param config An action to configure the values of the constructed [MavenArtifact].
     */
    val artifacts: Set<Artifact>? = null,
    /**
     * Sets the groupId for this publication.
     */
    val groupId: String? = null,
    /**
     * Sets the artifactId for this publication.
     */
    val artifactId: String? = null,
    /**
     * Sets the version for this publication.
     */
    val version: String? = null,
    /**
     * Configures the version mapping strategy.
     *
     * For example, to use resolved versions for runtime dependencies:
     * <pre class='autoTested'>
     * plugins {
     * id 'java'
     * id 'maven-publish'
     * }
     *
     * publishing {
     * publications {
     * maven(MavenPublication) {
     * from components.java
     * versionMapping {
     * usage('java-runtime'){
     * fromResolutionResult()
     * }
     * }
     * }
     * }
     * }
    </pre> *
     *
     * @param configureAction the configuration
     *
     * @since 5.2
     */
    val versionMapping: VersionMappingStrategy? = null,
    /**
     * Silences the compatibility warnings for the Maven publication for the specified variant.
     *
     * Warnings are emitted when Gradle features are used that cannot be mapped completely to Maven POM.
     *
     * @param variantName the variant to silence warning for
     *
     * @since 6.0
     */
    val suppressPomMetadataWarningsFor: List<String>? = null,
    /**
     * Silences all the compatibility warnings for the Maven publication.
     *
     * Warnings are emitted when Gradle features are used that cannot be mapped completely to Maven POM.
     *
     * @since 6.0
     */
    val suppressAllPomMetadataWarnings: Boolean? = null,
) : Publication<MavenPublication> {

    context(Project)
    override fun applyTo(receiver: MavenPublication) {
        super.applyTo(receiver)

        pom?.applyTo(receiver.pom)
        from?.let(components::getByName)?.let(receiver::from)

        artifacts?.forEach { artifact ->
            artifact.artifact?.also { mavenArtifact ->
                receiver.artifact(artifact.source, mavenArtifact::applyTo)
            } ?: receiver.artifact(artifact.source)

        }

        receiver.groupId = groupId ?: project.group.toString()
        receiver.artifactId = artifactId ?: project.name
        receiver.version = version ?: project.version.toString()
        receiver::versionMapping tryApply versionMapping?.let { versionMapping -> versionMapping::applyTo }
        suppressPomMetadataWarningsFor?.forEach(receiver::suppressPomMetadataWarningsFor)
        receiver::suppressAllPomMetadataWarnings trySet suppressAllPomMetadataWarnings
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.publishing.publications.withType<MavenPublication>()) { name, action ->
            project.publishing.publications.register(name, MavenPublication::class.java, action)
        }
}
