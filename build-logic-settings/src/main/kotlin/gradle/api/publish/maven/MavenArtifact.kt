package gradle.api.publish.maven

import gradle.api.publish.PublicationArtifact
import kotlinx.serialization.Serializable
import org.gradle.api.publish.maven.MavenArtifact

/**
 * An artifact published as part of a [MavenPublication].
 */
@Serializable
internal data class MavenArtifact(
    override val builtBy: List<String>? = null,
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

    override fun applyTo(recipient: MavenArtifact) {
        super.applyTo(recipient)

        extension?.let(recipient::setExtension)
        classifier?.let(recipient::setClassifier)
    }
}
