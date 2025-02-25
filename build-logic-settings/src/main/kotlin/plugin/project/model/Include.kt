package plugin.project.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Include(
    val name: String,
    val path: String,
)
