package plugin.project.kotlin.model

import kotlinx.serialization.Serializable

@Serializable
internal data class TargetGroup(
    val name: String,
    val group: List<String>
)
