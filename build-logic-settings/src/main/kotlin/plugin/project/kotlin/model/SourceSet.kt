package plugin.project.kotlin.model

import kotlinx.serialization.Serializable
import plugin.model.dependency.DependencyNotation

@Serializable
internal data class SourceSet(
    val name: String,
    val dependencies: List<DependencyNotation>? = null,
)
