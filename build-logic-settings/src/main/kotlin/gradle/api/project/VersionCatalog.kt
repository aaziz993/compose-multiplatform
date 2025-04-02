package gradle.api.project

import gradle.api.artifacts.Dependency
import kotlinx.serialization.Serializable

@Serializable
internal data class VersionCatalog(
    val name: String,
    val from: Dependency,
) {

    override fun equals(other: Any?): Boolean = (other is VersionCatalog)
        && name == other.name

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + from.hashCode()
        return result
    }
}
