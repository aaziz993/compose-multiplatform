package plugin.project.model.project

import kotlinx.serialization.Serializable
import plugin.project.gradle.develocity.model.DevelocitySettings
import plugin.project.gradle.model.ProjectGradleSettings

@Serializable
internal data class ProjectSettings(
    val gradle: ProjectGradleSettings = ProjectGradleSettings(),
)
