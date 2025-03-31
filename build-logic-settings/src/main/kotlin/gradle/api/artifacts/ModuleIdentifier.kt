package gradle.api.artifacts

import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.ModuleIdentifier

@Serializable
internal data class ModuleIdentifier(
    val group: String? = null,
    val name: String? = null,
) {

    override fun equals(other: Any?): Boolean =
        super.equals(other) || (other is ModuleIdentifier &&
            (group ?: other.group) == other.group && (name ?: other.name) == other.name
            )

    override fun hashCode(): Int {
        var result = group?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        return result
    }
}
