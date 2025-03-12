package gradle.plugins.toolchainmanagement

import org.gradle.api.initialization.Settings

internal interface ToolchainManagement {

    context(Settings)
    fun applyTo() {
    }
}
