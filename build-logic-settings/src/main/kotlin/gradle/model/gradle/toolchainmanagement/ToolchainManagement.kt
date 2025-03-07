package gradle.model.gradle.toolchainmanagement

import org.gradle.api.initialization.Settings

internal interface ToolchainManagement {

    context(Settings)
    fun applyTo() {
    }
}
