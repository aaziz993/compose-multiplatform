package gradle.api.artifacts

import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.ModuleVersionIdentifier

@Serializable
internal data class ModuleVersionIdentifier(
    val version: String? = null,
    val group: String? = null,
    val name: String? = null,
    val module: ModuleIdentifier? = null,
) {

    fun equals(other: ModuleVersionIdentifier): Boolean =
        (version ?: other.version) == other.version
            && (group ?: other.group) == other.group
            && (name ?: other.name) == other.name
            && module?.equals(other) != false
}
