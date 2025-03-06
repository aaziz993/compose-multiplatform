package gradle.model.project

import kotlinx.serialization.Serializable

@Serializable
internal data class PluginManagement(
    val repositories: List<String> = emptyList(),
)
