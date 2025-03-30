package gradle.plugins.toolchainmanagement

import gradle.accessors.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

internal class ToolchainManagementPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        with(target) {
            // Apply toolchainManagement properties.
            projectProperties.toolchainManagement?.applyTo()
        }
    }
}
