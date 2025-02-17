package plugin.project.gradle.toolchainmanagement.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ToolchainManagementSettings(
    val enabled: Boolean = true,
) : ToolchainManagement
