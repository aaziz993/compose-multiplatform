package gradle.project

import kotlinx.serialization.Serializable

@Serializable
internal data class VersionCatalog(
    val name: String,
    val from: @Serializable(with = DependencyKeyTransformingSerializer::class) Dependency,
) {

    override fun equals(other: Any?): Boolean = (other is VersionCatalog)
        && name == other.name

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + from.hashCode()
        return result
    }
}
