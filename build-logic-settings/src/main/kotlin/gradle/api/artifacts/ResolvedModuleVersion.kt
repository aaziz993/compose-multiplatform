package gradle.api.artifacts

import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.ResolvedModuleVersion

@Serializable
internal data class ResolvedModuleVersion(
    val id: ModuleVersionIdentifier? = null,
) {

    override fun equals(other: Any?) =
        super.equals(other) || (other is ResolvedModuleVersion &&
            (id ?: other.id) == other.id
            )

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
