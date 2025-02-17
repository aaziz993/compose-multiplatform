package plugin.settings.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ProjectProperties(
    val pluginManagement: PluginManagement = PluginManagement(),
    val dependencyResolutionManagement: DependencyResolutionManagement = DependencyResolutionManagement(),
    val modules: List<String> = emptyList(),
    val settings: ProjectSettings = ProjectSettings()
)
