package plugin.project.model.project

import kotlinx.serialization.Serializable
import plugin.project.gradle.develocity.model.DevelocitySettings

@Serializable
internal data class ProjectSettings(
    val develocity: DevelocitySettings = DevelocitySettings(),
)
