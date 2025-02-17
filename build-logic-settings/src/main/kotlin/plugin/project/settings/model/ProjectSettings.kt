package plugin.project.settings.model

import kotlinx.serialization.Serializable
import plugin.project.gradle.model.ProjectGradleSettings

@Serializable
internal data class ProjectSettings(
    val gradle: ProjectGradleSettings = ProjectGradleSettings(),
)
