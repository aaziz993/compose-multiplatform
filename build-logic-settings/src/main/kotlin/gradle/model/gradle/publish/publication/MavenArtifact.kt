package gradle.model.gradle.publish.publication

import kotlinx.serialization.Serializable
import org.gradle.api.Buildable
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
) : PublicationArtifact {

    override fun applyTo(buildable: Buildable) {
        super.applyTo(buildable)

        buildable as MavenArtifact

        extension?.let(buildable::setExtension)
        classifier?.let(buildable::setClassifier)
    }
}
