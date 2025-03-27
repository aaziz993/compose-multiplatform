package gradle.api.publish.maven

import gradle.api.publish.PublicationArtifact
import gradle.api.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.publish.maven.MavenArtifact

/**
 * An artifact published as part of a [MavenPublication].
 */
@Serializable
internal data class MavenArtifact(
    override val builtBy: Set<String>? = null,
    /**
     * Sets the extension used to publish the artifact file.
     * @param extension The extension.
     */
    val extension: String? = null,

    /**
     * Sets the classifier used to publish the artifact file.
     * @param classifier The classifier.
     */
    val classifier: String? = null,
) : PublicationArtifact<MavenArtifact> {

    override fun applyTo(receiver: MavenArtifact) {
        super.applyTo(receiver)

        receiver::setExtension trySet extension
        receiver::setClassifier trySet classifier
    }
}
