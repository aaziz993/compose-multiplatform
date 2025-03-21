package gradle.api.artifacts

import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.component.ComponentArtifactIdentifier

@Serializable
internal data class ComponentArtifactIdentifier(
    val componentIdentifier: ComponentIdentifier? = null,
    val displayName: String? = null,
) {

    fun equals(other: ComponentArtifactIdentifier) =
        componentIdentifier?.equals(other.componentIdentifier) != false
            && (displayName ?: other.displayName) == other.displayName
}
