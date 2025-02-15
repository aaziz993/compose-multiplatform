package plugin.project.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Alias(
    val name: String,
    val group: List<String>
)
