package plugin.project.model.project

import kotlinx.serialization.Serializable

@Serializable
internal data class ProjectProperties(
    val pluginManagement: PluginManagement = PluginManagement(),
    val modules: List<String> = emptyList(),
    val settings: ProjectSettings = ProjectSettings()
)
