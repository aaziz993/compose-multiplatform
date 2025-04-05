package gradle.plugins.toolchainmanagement

import gradle.api.initialization.initializationProperties
import gradle.api.project.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

internal class ToolchainManagementPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        with(target) {
            // Apply toolchainManagement properties.
            initializationProperties.toolchainManagement?.applyTo()
        }
    }
}
