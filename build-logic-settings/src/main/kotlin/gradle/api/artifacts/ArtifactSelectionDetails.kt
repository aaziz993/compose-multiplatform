package gradle.api.artifacts

import klib.data.type.collection.tryAddAll
import klib.data.type.collection.trySet
import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.ArtifactSelectionDetails
import org.gradle.api.artifacts.DependencyArtifactSelector

/**
 * Details about artifact dependency substitution: this class gives access to the
 * original dependency requested artifacts, if any, and gives the opportunity to
 * replace the original requested artifacts with other artifacts.
 *
 * This can typically be used whenever you need to substitute a dependency with
 * uses a classifier to a non-classified dependency, or the other way around.
 *
 * @since 6.6
 */
@Serializable
internal data class ArtifactSelectionDetails(
    /**
     * Returns the list of requested artifacts for the dependency
     */
    val requestedSelectors: List<DependencyArtifactSelector>? = null,
    val setRequestedSelectors: List<DependencyArtifactSelector>? = null,
    /**
     * Removes all artifact selectors, if any.
     */
    val withoutArtifactSelectors: Boolean? = null,
) {

    fun applyTo(receiver: ArtifactSelectionDetails) {
        receiver.requestedSelectors tryAddAll requestedSelectors
        receiver.requestedSelectors trySet requestedSelectors
        receiver::withoutArtifactSelectors trySet withoutArtifactSelectors
    }
}


