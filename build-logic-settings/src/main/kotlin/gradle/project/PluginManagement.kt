package gradle.project

import kotlinx.serialization.Serializable

@Serializable
internal data class PluginManagement(
    val repositories: List<String> = emptyList(),
)
