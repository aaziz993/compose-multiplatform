package plugin.project.kotlin.model.language

import kotlinx.serialization.Serializable
import plugin.model.dependency.Dependency

@Serializable
internal data class SourceSet(
    val dependencies: List<Dependency>? = null,
)
