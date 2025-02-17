package plugin.project.model.module

import kotlinx.serialization.Serializable

@Serializable
internal data class Alias(
    val name: String,
    val group: List<String>
)
