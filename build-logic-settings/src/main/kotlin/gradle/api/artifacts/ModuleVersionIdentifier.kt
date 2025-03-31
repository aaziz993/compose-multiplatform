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

    override fun equals(other: Any?): Boolean =
        super.equals(other) || (other is ModuleVersionIdentifier &&
            (version ?: other.version) == other.version
            && (group ?: other.group) == other.group
            && (name ?: other.name) == other.name
            && module?.equals(other) != false
            )

    override fun hashCode(): Int {
        var result = version?.hashCode() ?: 0
        result = 31 * result + (group?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (module?.hashCode() ?: 0)
        return result
    }
}
