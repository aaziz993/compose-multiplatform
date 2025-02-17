package plugin.project.gradle.model

import kotlinx.serialization.Serializable
import plugin.project.gradle.develocity.model.DevelocitySettings
import plugin.project.gradle.githooks.model.GitHooksSettings
import plugin.project.gradle.toolchainmanagement.model.ToolchainManagementSettings

@Serializable
internal data class ProjectGradleSettings(
    val gradleEnterpriseAccessKey: String? = null,
    val develocity: DevelocitySettings = DevelocitySettings(),
    val toolchainManagement: ToolchainManagementSettings = ToolchainManagementSettings(),
    val gitHooks: GitHooksSettings = GitHooksSettings(),
)
