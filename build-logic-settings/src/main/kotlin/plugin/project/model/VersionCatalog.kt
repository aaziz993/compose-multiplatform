package plugin.project.model

import kotlinx.serialization.Serializable
import plugin.model.dependency.DependencyNotation

@Serializable
internal data class VersionCatalog(
    val name: String,
    val from: DependencyNotation,
)
