package gradle.api.artifacts

import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.component.ComponentIdentifier

@Serializable
internal data class ComponentIdentifier(
    val displayName: String? = null
) {

    override fun equals(other: Any?) =
        super.equals(other) || (other is ComponentIdentifier &&
            (displayName ?: other.displayName) == other.displayName
            )

    override fun hashCode(): Int {
        return displayName?.hashCode() ?: 0
    }
}
