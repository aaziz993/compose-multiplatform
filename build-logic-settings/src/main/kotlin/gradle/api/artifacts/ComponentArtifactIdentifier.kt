package gradle.api.artifacts

import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.component.ComponentArtifactIdentifier

@Serializable
internal data class ComponentArtifactIdentifier(
    val componentIdentifier: ComponentIdentifier? = null,
    val displayName: String? = null,
) {

    override fun equals(other: Any?) =
        super.equals(other) || (other is ComponentArtifactIdentifier &&
            componentIdentifier?.equals(other.componentIdentifier) != false
            && (displayName ?: other.displayName) == other.displayName
            )

    override fun hashCode(): Int {
        var result = componentIdentifier?.hashCode() ?: 0
        result = 31 * result + (displayName?.hashCode() ?: 0)
        return result
    }
}
