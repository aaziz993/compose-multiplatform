package plugin.settings.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ProjectProperties(
    val pluginManagement: PluginManagement?=null,
    val dependencyResolutionManagement: DependencyResolutionManagement? = null,
    val modules: List<String>?=null,
    val settings: ProjectSettings = ProjectSettings()
)
