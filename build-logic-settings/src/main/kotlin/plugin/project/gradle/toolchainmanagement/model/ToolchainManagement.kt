package plugin.project.gradle.toolchainmanagement.model

import org.gradle.api.initialization.Settings

internal interface ToolchainManagement {

    context(Settings)
    fun applyTo() {
    }
}
