package gradle.api.artifacts

import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.ModuleIdentifier

@Serializable
internal data class ModuleIdentifier(
    val group: String? = null,
    val name: String? = null,
) {

    fun equals(other: ModuleIdentifier): Boolean =
        (group ?: other.group) == other.group && (name ?: other.name) == other.name
}
