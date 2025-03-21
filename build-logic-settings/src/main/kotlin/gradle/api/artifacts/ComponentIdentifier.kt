package gradle.api.artifacts

import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.component.ComponentIdentifier

@Serializable
internal data class ComponentIdentifier(
    val displayName: String? = null
) {

    fun equals(other: ComponentIdentifier) =
        (displayName ?: other.displayName) == other.displayName
}
