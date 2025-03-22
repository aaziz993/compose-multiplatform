package gradle.plugins.toolchainmanagement

import org.gradle.api.initialization.Settings

internal interface ToolchainManagement {

    context(Settings)
    @Suppress("UnstableApiUsage")
    fun applyTo() {
        toolchainManagement {

        }
    }
}
