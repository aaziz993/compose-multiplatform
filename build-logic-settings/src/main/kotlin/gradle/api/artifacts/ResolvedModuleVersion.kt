package gradle.api.artifacts

import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.ResolvedModuleVersion

@Serializable
internal data class ResolvedModuleVersion(
    val id: ModuleVersionIdentifier? = null,
) {

    fun equals(other: ResolvedModuleVersion) =
        id == null || id.equals(other.id)
}
