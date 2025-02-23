package plugin.project.model

import kotlinx.serialization.Serializable
import plugin.model.dependency.Dependency

@Serializable
internal data class VersionCatalog(
    val name: String,
    val from: Dependency,
){

    override fun equals(other: Any?): Boolean =(other is VersionCatalog)
        && name == other.name

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + from.hashCode()
        return result
    }
}
