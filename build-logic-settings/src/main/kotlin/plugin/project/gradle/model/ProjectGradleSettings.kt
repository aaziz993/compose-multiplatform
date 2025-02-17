package plugin.project.gradle.model

import kotlinx.serialization.Serializable
import plugin.project.gradle.githooks.model.GitHooksSettings
import plugin.project.gradle.develocity.model.DevelocitySettings

@Serializable
internal data class ProjectGradleSettings(
    val develocity: DevelocitySettings = DevelocitySettings(),
    val gitHooks: GitHooksSettings = GitHooksSettings()
)
