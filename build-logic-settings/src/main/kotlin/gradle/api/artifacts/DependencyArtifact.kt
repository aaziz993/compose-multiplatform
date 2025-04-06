package gradle.api.artifacts

import klib.data.type.reflection.trySet
import org.gradle.api.artifacts.DependencyArtifact

/**
 * Data class that represents an artifact included in a [org.gradle.api.artifacts.Dependency].
 */
internal data class DependencyArtifact(
    /**
     * Sets the name of this artifact.
     */
    val name: String,
    /**
     * Sets the type of this artifact.
     */
    val type: String,
    /**
     * Sets the extension of this artifact.
     */
    val extension: String? = null,
    /**
     * Sets the classifier of this artifact.
     */
    val classifier: String? = null,
    /**
     * Sets the URL for this artifact.
     */
    val url: String? = null,
) {

    fun applyTo(receiver: DependencyArtifact) {
        receiver::setName trySet name
        receiver::setType trySet type
        receiver::setClassifier trySet classifier
        receiver::setUrl trySet url
    }
}
